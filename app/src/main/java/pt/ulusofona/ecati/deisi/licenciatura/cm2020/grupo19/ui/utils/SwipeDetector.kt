package pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import pt.ulusofona.ecati.deisi.licenciatura.cm2020.grupo19.ui.listeners.OnSwipeDetected

class SwipeDetector(private val listener: OnSwipeDetected, private val color: Int) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val swipeBackground = ColorDrawable(this.color)
        val itemView = viewHolder.itemView
        if (dX > 0) {
            swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
        } else {
            swipeBackground.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
        }
        swipeBackground.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwipeDetected(viewHolder.adapterPosition)
    }

}