package template.composemultiplatform.html.test.common

import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.NodeList
import org.w3c.dom.get

fun NodeList.findChild(id: String): HTMLDivElement? {
    (0 until length).forEach { index ->
        val child = get(index) as? Element
        if (child?.id == id) {
            return child as? HTMLDivElement
        }

        child?.childNodes?.findChild(id)?.let {
            return it
        }
    }

    return null
}

/**
 * Indeterminate progresses are snapshot at random positions. Thus make them determinate at 50/100
 */
fun Element.setIndeterminateProgressToZero(): Element {
    with(this.children) {
        (0 until length).forEach { index ->
            val child = get(index)
            when (child?.tagName) {
                "MD-CIRCULAR-PROGRESS", "MD-LINEAR-PROGRESS" -> {
                    if (child.hasAttribute("indeterminate")) {
                        child.removeAttribute("indeterminate")
                        child.setAttribute("value", "50")
                        child.setAttribute("max", "100")
                    }
                }
                else -> child?.setIndeterminateProgressToZero()
            }
        }
    }

    return this
}
