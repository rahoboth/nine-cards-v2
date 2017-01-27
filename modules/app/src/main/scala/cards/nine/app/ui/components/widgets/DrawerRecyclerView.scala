package cards.nine.app.ui.components.widgets

import android.content.Context
import android.support.v7.widget.{GridLayoutManager, RecyclerView}
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.animation.GridLayoutAnimationController.AnimationParameters
import cards.nine.commons._
import macroid._

class DrawerRecyclerView(context: Context, attr: AttributeSet, defStyleAttr: Int)
    extends RecyclerView(context, attr, defStyleAttr)
    with Contexts[View] { self =>

  def this(context: Context) = this(context, javaNull, 0)

  def this(context: Context, attr: AttributeSet) = this(context, attr, 0)

  var statuses = DrawerRecyclerStatuses()

  override def attachLayoutAnimationParameters(
      child: View,
      params: LayoutParams,
      index: Int,
      count: Int): Unit =
    Option(getLayoutManager) match {
      case (Some(layoutManager: GridLayoutManager)) =>
        val animationParams = Option(params.layoutAnimationParameters) match {
          case Some(animParams: AnimationParameters) => animParams
          case _ =>
            val animParams = new AnimationParameters()
            params.layoutAnimationParameters = animParams
            animParams
        }
        val columns = layoutManager.getSpanCount
        animationParams.count = count
        animationParams.index = index
        animationParams.columnsCount = columns
        animationParams.rowsCount = count / columns
        val invertedIndex = count - 1 - index
        animationParams.column = columns - 1 - (invertedIndex % columns)
        animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns
      case _ =>
        super.attachLayoutAnimationParameters(child, params, index, count)
    }

}

case class DrawerRecyclerStatuses(contentView: ContentView = AppsView)

sealed trait ContentView

case object AppsView extends ContentView

case object ContactView extends ContentView