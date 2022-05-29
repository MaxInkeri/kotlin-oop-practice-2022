package enums

import components.PropertySpinner

enum class PropertySpinnerInfo(val prop: String, var spinner: PropertySpinner? = null) {
    WIDTH("Width"), HEIGHT("Height"), THICKNESS("Thickness");

    companion object {
        fun forEach(lambda: (PropertySpinnerInfo) -> Unit) {
            values().forEach {
                lambda(it)
            }
        }
    }
}