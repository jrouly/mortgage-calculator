# mortgage-calculator

Simple mortgage math utility in Scala.
Provides two primary functions:

```scala
// Given a home value, down payment, and mortgage parameters,
// compute (and break down) a monthly cost.
// Does not factor in static costs like HOA, mortgage insurance, etc.
Calculations.calculateMonthlyPayment(
  homeValue = 100000,
  downPayment = Left(20000), // or Right(percentage)
  mortgageRate = 0.04,
  mortgageTerm = Period.ofYears(30),
  propertyTaxRate = 0.01,
)
```

```scala
// Given annual income, a target debt/income ratio, and a down payment amount,
// compute the total home value with a monthly payment that meets the target ratio.
// Does not factor in static costs like HOA, mortgage insurance, etc.
Calculations.calculateAffordableBudget(
  annualGrossIncome = 60000,
  debtIncomeRatio = 0.33,
  downPayment = 20000,
  mortgageRate = 0.04,
  mortgageTerm = Period.ofYears(30),
  propertyTaxRate = 0.01,
)
```
