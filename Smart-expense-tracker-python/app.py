import streamlit as st
import pandas as pd
import matplotlib.pyplot as plt
import os
from datetime import date

#Initializing dataset
File_name = "expenses.csv"

def initialize_file():
    if not os.path.exists(File_name):
        df = pd.DataFrame(columns=["Date","Category","Amount","Type","Description","PaymentMethod","User","Location","Recurring"])

#Load data
def load_data():
    if not os.path.exists(File_name):
        return pd.DataFrame(columns=["Date","Category","Amount","Type","Description","PaymentMethod","User","Location","Recurring"])
    df = pd.read_csv(File_name)

    if df.empty:
        return df
    df["Date"] = pd.to_datetime(df["Date"],errors="coerce")
    df=df.dropna(subset=["Date"])
    return df

#Add expense
def add_expense(data):
    df = load_data()
    new_df = pd.DataFrame([data])
    df = pd.concat([df,pd.DataFrame([data])],ignore_index=True)
    df.to_csv(File_name,index = False)

#Monthly analysis
def analyze_month(month):
    df=load_data()
    month_df=df[df["Date"].dt.strftime("%Y-%m") == month]

    if month_df.empty:
        return None
    
    insights = {}

    insights["total"] = month_df["Amount"].sum()
    insights["category_breakdown"] = month_df.groupby("Category")["Amount"].sum()
    if insights["category_breakdown"].empty:
        insights["highest_category"] = "No data"
    else:
        insights["highest_category"] = insights["category_breakdown"].idxmax()
    insights["avg_daily"] = insights["total"]/month_df["Date"].nunique()
    insights["cashless_ratio"] = (month_df[month_df["PaymentMethod"] != "Cash"].shape[0]/month_df.shape[0])*100
    insights["recurring_cost"] = month_df[month_df["Recurring"] == "Yes"]["Amount"].sum()
    location_data = month_df.groupby("Location")["Amount"].sum()
    if location_data.empty:
        insights["top_location"] = "No data"
    else:
        insights["top_location"] = location_data.idxmax()

    return insights

#Streamlit UI
st.set_page_config(page_title="Smart Expense Tracker",layout="centered")
st.title("💡Smart Expense Tracker With Insights")

initialize_file()
menu = st.sidebar.selectbox("Select Option",["Add Expense","Monthly Insights"])

#Add Expense
if menu == "Add Expense":
    st.subheader("➕Record New Expense")
    data = {
        "Date" : st.date_input("Date",date.today()).strftime("%Y-%m-%d"),
        "Category" : st.selectbox("Category",["Food","Travel","Bills","Shopping","Entertainment","Health","Other"]),
        "Amount" : st.number_input("Amount(₹)",min_value=0.0),
        "Type" : st.selectbox("Type",["Need","Want"]),
        "Description" : st.text_input("Description"),
        "PaymentMethod" : st.selectbox("PaymentMethod",["UPI","Card","Cash","NetBanking"]),
        "User" : st.text_input("User Name"),
        "Location" : st.text_input("Location"),
        "Recurring" : st.selectbox("Recurring Expense?",["Yes","No"])
    }

    if st.button("Save Expense"):
        add_expense(data)
        st.success("Expense recorded successfully!")

#Monthly Insights
else:
    st.subheader("📊Monthly Expense Insights")

    selected_date = st.date_input("Select any date in month")
    month = selected_date.strftime("%Y-%m")

    if st.button("Generate Insights"):
        insights = analyze_month(month)

        if insights is None:
            st.warning("No data found for this month.")
        else:
            st.metric("Total Monthly Spend",f"₹{insights['total']:.2f}")
            st.metric("Average Daily Spend",f"₹{insights['avg_daily']:.2f}")

            st.info(f"🔺Highest Spending Category: **{insights['highest_category']}**")
            st.info(f"📍Highest Spending Location: **{insights['top_location']}**")
            st.info(f"💳Cashless Transactions: **{insights['cashless_ratio']:.1f}%**")
            st.info(f"🔁Recurring Monthly Cost: **₹ {insights['recurring_cost']:.2f}%**")

            #Pie Chart
            fig, ax = plt.subplots()
            ax.pie(
                insights["category_breakdown"].values,
                labels=insights["category_breakdown"].index,
                autopct="%1.1f%%",
                startangle=90
            )

            ax.set_title("Category-wise Expense Distribution")
            st.pyplot(fig)

            #Smart Suggestions
            st.subheader("💡Smart Spending Suggestions")

            if insights["highest_category"] in ["Food","Entertainment"]:
                st.warning("Consider reducing discretionary spending to save more.")
            if insights["recurring_cost"] > insights["total"]*0.4:
                st.warning("High recurring costs deducted. Review Subscriptions.")
            if insights["avg_daily"]>1000:
                st.warning("Daily average spending is high. Consider budgeting.")

            st.success("Insight generation completed successfully!")
















