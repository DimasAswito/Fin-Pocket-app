package com.example.finpocket.model

data class PlanRequest(
    val plan: Plan,
    val budgets: List<Budget>
)

data class Plan(
    val income: Int,
    val age: Int,
    val dependents: Int
)

data class Budget(
    val category_id: Int,
    val percentage: Double,
    val amount: Int
)
