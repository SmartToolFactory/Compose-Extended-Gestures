# Jetpack Compose Gestures

[![](https://jitpack.io/v/SmartToolFactory/Compose-Extended-Gestures.svg)](https://jitpack.io/#SmartToolFactory/Compose-Extended-Gestures)

Jetpack Compose gesture library that expands avaiable gesture functions with onTouchEvent counterpart of event, transform and touch delegate gestures.


https://user-images.githubusercontent.com/35650605/177026717-2819c0dd-e43d-466f-bfc0-2dcad268ae40.mp4



## onTouch Event for Jetpack Compose

### Modifier.pointerMotionEvents

Creates a modifier for processing pointer motion input within the region of the modified element.

After `AwaitPointerEventScope.awaitFirstDown` returns a `PointerInputChange` followed by `onDown`
at first pointer contact. Moving any pointer invokes `AwaitPointerEventScope.awaitPointerEvent`
then `onMove` is called. When last pointer is up `onUp` is called.

To prevent other pointer functions that call `awaitFirstDown`
or `awaitPointerEvent` (scroll, swipe, detect functions)
call `PointerInputChange.consume()` in `onDown`, and call `PointerInputChange.consume()` in `onMove`
block.

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

### Modifier.pointerMotionEventList

pointerMotionEventList returns list of pointers in `onMove`

```kotlin
fun Modifier.pointerMotionEventList(
    key1: Any? = Unit,
    onDown: (PointerInputChange) -> Unit = {},
    onMove: (List<PointerInputChange>) -> Unit = {},
    onUp: (PointerInputChange) -> Unit = {},
    delayAfterDownInMillis: Long = 0L
) 
```

* `delayAfterDownInMillis` parameter invokes Coroutines delay between `onDown`, and `onMove`. There
  is a delay about 20ms between in View's `onTouchEvent` first touch and move, similar delay might
  be required with Compose too, especially when drawing to `Canvas` which misses very fast events,
  Delaying move behavior might be required to detect whether is touch is in required region of
  Composable at first pointer contact.

`PointerInputChange` down and move events should be consumed if you need to prevent other gestures
like **scroll** or other **pointerInput**s to not intercept your gesture

```kotlin
 Modifier.pointerMotionEvents(
    onDown = {
        // When down is consumed
        it.consume()
    },
    onMove = {
        // Consuming move prevents scroll other events to not get this move event
        it.consume()
    },
    delayAfterDownInMillis = 20
)
```

You can refer [this answer](https://stackoverflow.com/a/70847531/5457853) for details.

## Transform Gestures
### detectTransformGesturesAndChanges

A gesture detector for rotation, panning, and zoom. Once touch slop has been reached, the user can
use rotation, panning and zoom gestures. `onGesture` will be called when any of the rotation, zoom
or pan occurs, passing the rotation angle in degrees, zoom in scale factor and pan as an offset in
pixels. Each of these changes is a difference between the previous call and the current gesture.
This will consume all position changes after touch slop has been reached. onGesture will also
provide centroid of all the pointers that are down.

After gesture started when last pointer is up `onGestureEnd` is triggered.
`pointerList` returns info about pointers that are available to this gesture. 
`mainPointerInputChange` is the first pointer that is down initially, if it's lifted while
other pointers are down `mainPointer` is set the first one in `pointerList`

Usage

```kotlin
Modifier.pointerInput(Unit) {
  detectTransformGestures(
    onGestureStart = {
      transformDetailText = "GESTURE START"
    },
    onGesture = { gestureCentroid: Offset,
                  gesturePan: Offset,
                  gestureZoom: Float,
                  gestureRotate: Float,
                  mainPointerInputChange: PointerInputChange,
                  pointerList: List<PointerInputChange> ->
     
    },
    onGestureEnd = {
      borderColor = Color.LightGray
      transformDetailText = "GESTURE END"
    }
  )
}
```

### detectPointerTransformGestures

Transform gesture as `detectTransformGestures` except with `gestureEnd` callback, returns number of
pointers that are down and checks for requisite and number of pointers before continuing transform
gestures. when requisite is not met gesture is on hold and ends when last pointer is up. This might
be useful in scenarios like not panning when pointer number is higher than 1, or scenarios require
specific conditions to be met

```kotlin
Modifier
    .pointerInput(Unit) {
        detectPointerTransformGestures(
            numberOfPointers = 1,
            requisite = PointerRequisite.GreaterThan,
          onGestureStart = {
            transformDetailText = "GESTURE START"
          },
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

## TouchDelegate for Jetpack Compose
Modifier to handle situations where you want a view to have a larger touch area than  
its actual Composable bounds. [dpRect] increases when values are positive and  
decreases touch area by negative values entered for any side

```
fun Modifier.touchDelegate(
    dpRect: DelegateRect = DelegateRect.Zero,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) =
    composed(
        inspectorInfo = {
            name = "touchDelegate"
            properties["dpRect"] = dpRect
            properties["enabled"] = enabled
            properties["onClickLabel"] = onClickLabel
            properties["role"] = role
            properties["onClick"] = onClick
        },
        factory = {

            Modifier.touchDelegate(
                dpRect = dpRect,
                enabled = enabled,
                onClickLabel = onClickLabel,
                onClick = onClick,
                role = role,
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            )
        }
    )
```

To increase touch area of Composable without changing its dimensions and scale
set a `DelegateRect(left,top,right,bottom)` with positive values to increase touch area on
sides specified by rectangle or negative values to decrease touch area.

## Gesture Tutorial

If you need more detailed tutorial about Jetpack Compose gestures
check [this tutorial](https://github.com/SmartToolFactory/Jetpack-Compose-Tutorials#gesture)

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
