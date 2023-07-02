package net.limbuserendipity.customjuicecompose.util

import net.limbuserendipity.customjuicecompose.ui.model.Ingredient
import net.limbuserendipity.customjuicecompose.ui.theme.*

val appleIngredient
    get() = Ingredient(
        name = "Apple",
        emoji = appleEmoji,
        color = appleColor
)
val lemonIngredient
    get() = Ingredient(
        name = "Lemon",
        emoji = lemonEmoji,
        color = lemonColor
)
val grapeIngredient
    get() = Ingredient(
        name = "Grape",
        emoji = grapeEmoji,
        color = grapeColor
)
val strawberryIngredient
    get() = Ingredient(
        name = "Strawberry",
        emoji = strawberryEmoji,
        color = strawberryColor
)
val peachIngredient
    get() = Ingredient(
        name = "Peach",
        emoji = peachEmoji,
        color = peachColor
)
val coconutIngredient
    get() = Ingredient(
        name = "Coconut",
        emoji = coconutEmoji,
        color = coconutColor
)

val ingredientList
    get() = listOf(
        appleIngredient,
        lemonIngredient,
        grapeIngredient,
        strawberryIngredient,
        peachIngredient,
        coconutIngredient
    )