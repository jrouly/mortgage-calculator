# mortgage-calculator

Simple mortgage math utility in Scala.
Provides two primary functions: calculating a monthly payment from a home value and mortgage parameters, and calculating the maximum affordable house given personal finance parameters.

### Monthly mortgage payment

Given a home value, down payment, and mortgage parameters, compute (and break down) a monthly cost.
Does not factor in static costs like HOA, mortgage insurance, etc.

```scala
Calculations.calculateMonthlyPayment(
  homeValue = 100000,
  downPayment = Left(20000), // or Right(percentage)
  mortgageRate = 0.04,
  mortgageTerm = Period.ofYears(30),
  propertyTaxRate = 0.01,
)
```

output:

```
// INPUT
//   home value: $100000.00
//   down payment: $20000.00 (20%)
//   loan principle: $80000.00
// OUTPUT
//   total monthly payment: $465.27
//     - loan repayment: $381.93
//     - property tax: $83.33
```

### Maximum affordable home price

Given annual income, a target debt/income ratio, and a down payment amount, compute the total home value with a monthly payment that meets the target ratio.
Does not factor in static costs like HOA, mortgage insurance, etc.

```scala
Calculations.calculateAffordableBudget(
  annualGrossIncome = 60000,
  debtIncomeRatio = 0.33,
  downPayment = 20000,
  mortgageRate = 0.04,
  mortgageTerm = Period.ofYears(30),
  propertyTaxRate = 0.01,
)
```

output:

```
INPUT
  annual gross income: $60000.00
  monthly gross income: $5000.00
  debt/income ratio: 33%
  down payment: $20000.00 (6%)
OUTPUT
  total monthly payment: $1650.00
    - loan repayment: $1391.00
    - property tax: $259.00 (estimate)
  loan principle: $291360.59
  home value: $311360.5
```
