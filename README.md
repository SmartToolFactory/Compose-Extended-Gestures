# Jetpack Compose Gestures

[![](https://jitpack.io/v/SmartToolFactory/Compose-Extended-Gestures.svg)](https://jitpack.io/#SmartToolFactory/Compose-Extended-Gestures)

Counterpart of `onTouchEvent` for Jetpack Compose and transform gesture with specific number of
pointers


https://user-images.githubusercontent.com/35650605/167546558-2779728f-d675-4499-b852-a0638c1cc8aa.mp4




## Modifier.pointerMotionEvents

Creates a modifier for processing pointer motion input within the region of the modified element.

After `AwaitPointerEventScope.awaitFirstDown` returns a `PointerInputChange` followed by `onDown` 
 at first pointer contact. Moving any pointer
invokes `AwaitPointerEventScope.awaitPointerEvent` then `onMove` is called. When last pointer is
up `onUp` is called. 

To prevent other pointer functions that call `awaitFirstDown`
or `awaitPointerEvent` (scroll, swipe, detect functions) 
call `PointerInputChange.consumeDownChange` in `onDown`, and
call `PointerInputChange.consumePositionChange` in `onMove` block.

```kotlin
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

Overload that returns list of pointers from `onMove`

```kotlin
fun Modifier.pointerMotionEventList(
    key1: Any? = Unit,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (List<PointerInputChange>) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L
) 
```

* `delayAfterDownInMillis` parameter invokes Coroutines delay between `onDown`, and `onMove`.
  There is a delay about 20ms between in View's `onTouchEvent` first touch and move, similar delay might
  be required with Compose too, especially when drawing to `Canvas` which misses very fast events,
  Delaying move behavior might be required to detect whether is touch is in
  required region of Composable at first pointer contact.

`PointerInputChange` down and move events should be consumed if you need to prevent other gestures
like **scroll** or other **pointerInput**s to not intercept your gesture

```kotlin
 Modifier.pointerMotionEvents(
    onDown = {
        // When down is consumed
        it.consumeDownChange()
    },
    onMove = {
        // Consuming move prevents scroll other events to not get this move event
        it.consumePositionChange()
    },
    delayAfterDownInMillis = 20
)
```

You can refer [this answer](https://stackoverflow.com/a/70847531/5457853) for details.

## Modifier.detectTransformGesturesAndChanges

A gesture detector for rotation, panning, and zoom. Once touch slop has been reached, the user can
use rotation, panning and zoom gestures. `onGesture` will be called when any of the rotation, zoom
or pan occurs, passing the rotation angle in degrees, zoom in scale factor and pan as an offset in
pixels. Each of these changes is a difference between the previous call and the current gesture.
This will consume all position changes after touch slop has been reached. onGesture will also
provide centroid of all the pointers that are down.

After gesture started when last pointer is up `onGestureEnd` is triggered.
`pointerList` returns info about pointers that are available to this gesture

Usage

```kotlin
Modifier.pointerInput(Unit) {
    detectTransformGesturesAndChanges(
        onGesture = { gestureCentroid: Offset,
                      gesturePan: Offset,
                      gestureZoom: Float,
                      gestureRotate: Float,
                      pointerList: List<PointerInputChange> ->

        },
        onGestureEnd = {
            transformDetailText = "GESTURE END"
        }
    )
}
```

## Modifier.detectTransformGesturesAndChanges
Transform gesture as `detectTransformGestures` except with `gestureEnd` callback, returns
number of pointers that are down and checks for requisite and number of pointers before continuing
transform gestures. when requisite is not met gesture is on hold and ends when last pointer
is up. This might be useful in scenarios like not panning when pointer number is higher than 1,
or scenarios require specific conditions to be met

```kotlin
Modifier
    .pointerInput(Unit) {
        detectPointerTransformGestures(
            numberOfPointers = 1,
            requisite = PointerRequisite.GreaterThan,
            onGesture = { gestureCentroid: Offset,
                          gesturePan: Offset,
                          gestureZoom: Float,
                          gestureRotate: Float,
                          numberOfPointers: Int ->

            },
            onGestureEnd = {
                transformDetailText = "GESTURE END"
            }
        )
    }
```

## Gradle Setup

To get a Git project into your build:

* Step 1. Add the JitPack repository to your build file Add it in your root build.gradle at the end
  of repositories:

```
allprojects {
  repositories {
      ...
      maven { url 'https://jitpack.io' }
  }
}
```

* Step 2. Add the dependency

```
dependencies {
  implementation 'com.github.SmartToolFactory:Compose-Extended-Gestures:Tag'
}
```
