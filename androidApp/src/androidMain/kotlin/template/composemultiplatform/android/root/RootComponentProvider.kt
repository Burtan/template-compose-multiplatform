package template.composemultiplatform.android.root

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import template.composemultiplatform.shared.root.RootComponentPreview

class RootComponentProvider : PreviewParameterProvider<RootComponentPreview> {
    override val values = RootComponentPreview.previews.asSequence()
}
