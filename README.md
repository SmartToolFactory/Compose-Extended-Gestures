### Jetpack Compose Gestures

Counterpart of `onTouchEvent` for Jetpack Compose and transform gesture with specific number of pointers

### Modifier.pointerMotionEvents

Creates a modifier for processing pointer motion input within the region of the modified element.

After `AwaitPointerEventScope.awaitFirstDown` returned a `PointerInputChange` then `onDown` is called at first pointer contact.
Moving any pointer causes `AwaitPointerEventScope.awaitPointerEvent` then `onMove` is called.
When last pointer is up `onUp` is called.
To prevent other pointer functions that call `awaitFirstDown` or `awaitPointerEvent` (scroll, swipe, detect functions) receiving changes call `PointerInputChange.consumeDownChange` in `onDown`, and call `PointerInputChange.consumePositionChange` in `onMove` block.

```
fun Modifier.pointerMotionEvents(
    vararg keys: Any?,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (PointerInputChange) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L
) = this.then(
    Modifier.pointerInput(keys) {
        detectMotionEvents(onDown, onMove, onUp, delayAfterDownInMillis)
    }
)
```

and the one returns list of pointer on move

```
fun Modifier.pointerMotionEventList(
    key1: Any? = Unit,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (List<PointerInputChange>) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L
) 
```

### Modifier.detectMultiplePointerTransformGestures
Returns the rotation, in degrees, of the pointers between the `PointerInputChange.previousPosition` and `PointerInputChange.position` states.
Only number of pointers that equal to `numberOfPointersRequired` that are down in both previous and current states are considered.

Usage

```
Modifier.pointerInput(Unit) {
    detectMultiplePointerTransformGestures(
        onGesture = { gestureCentroid, gesturePan, gestureZoom, gestureRotate ->
            val oldScale = zoom
            val newScale = zoom * gestureZoom

            // For natural zooming and rotating, the centroid of the gesture should
            // be the fixed point where zooming and rotating occurs.
            // We compute where the centroid was (in the pre-transformed coordinate
            // space), and then compute where it will be after this delta.
            // We then compute what the new offset should be to keep the centroid
            // visually stationary for rotating and zooming, and also apply the pan.
            offset = (offset + gestureCentroid / oldScale).rotateBy(gestureRotate) -
                    (gestureCentroid / newScale + gesturePan / oldScale)
            zoom = newScale.coerceIn(0.5f..5f)
            angle += gestureRotate

            transformDetailText =
                "Zoom: ${decimalFormat.format(zoom)}, centroid: $gestureCentroid\n" +
                        "angle: ${decimalFormat.format(angle)}, " +
                        "Rotate: ${decimalFormat.format(gestureRotate)}, pan: $gesturePan"
        }
    )
}
```