package com.example.workoutapp.data.activeworkout

sealed class DialogType {
    object DialogForEmptyWorkout : DialogType()
    object DialogForTemplateUnchangedWorkout : DialogType()
    object DialogForTemplateChangedWorkout : DialogType()
}