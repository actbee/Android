package com.example.xuedan_zou_myrun2

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.provider.MediaStore
import android.text.InputType
import android.widget.TextView
import java.util.*


class Comments_Dialogs: DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pref : SharedPreferences =requireActivity().getSharedPreferences("saved_profile_comment",
            Context.MODE_PRIVATE)
        val view = requireActivity().layoutInflater.inflate(
            R.layout.text_dialog, null
        )
        val edit = view.findViewById<TextView>(R.id.text_dialog)
        edit.text = pref.getString("settings_comment", "")
        edit.inputType = InputType.TYPE_CLASS_TEXT
        val builder=AlertDialog.Builder(requireContext())
            .setTitle("Comment")
            .setView(view)
            .setPositiveButton("OK") { _, _ ->
                pref.edit().putString("settings_comment",
                    edit.text.toString()).apply()
                dismiss()
            }
            .setNegativeButton("CANCEL") { _, _ ->
                dismiss()
            }

        return builder.create()
    }
}

class Unit_Preference_dialogs: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pref: SharedPreferences = requireActivity().getSharedPreferences(
            "unit",
            Context.MODE_PRIVATE
        )
        val view  = requireActivity().layoutInflater.inflate(R.layout.unit_dialog, null)
        val rg = view.findViewById<RadioGroup>(R.id.UnitPreferences)
        rg.check(pref.getInt("unit", 0))
        rg.setOnCheckedChangeListener { _, which ->
            pref.edit().putInt("unit", which).apply()
        }

        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Unit Preference")
            .setView(view)

        return builder.create()
    }
}

class Date_Dialogs: DialogFragment(), DatePickerDialog.OnDateSetListener {
    val pref : SharedPreferences =requireActivity().getSharedPreferences("saved_date",
        Context.MODE_PRIVATE)
    val calendar = Calendar.getInstance()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        val datePickerDialog = DatePickerDialog(
            requireContext(), this,
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel") { _, _ ->
            dismiss()
        }
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok") { _, _ ->
            pref.edit().putString("saved_date",
                Calendar.MONTH.toString()+Calendar.DAY_OF_MONTH.toString()+Calendar.YEAR.toString()).apply()
            dismiss()
        }
        return datePickerDialog
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
    }
}

class MyTimeDialog: DialogFragment(), TimePickerDialog.OnTimeSetListener{
    val pref : SharedPreferences =requireActivity().getSharedPreferences("saved_time",
        Context.MODE_PRIVATE)
    val calendar = Calendar.getInstance()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val timePickerDialog = TimePickerDialog(
            requireContext(), this,
            calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        )
        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel") { _, _ ->
            dismiss()
        }
        timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok") { _, _ ->
            pref.edit().putString("saved_time",
                 Calendar.HOUR_OF_DAY.toString()+":"+Calendar.MINUTE.toString()).apply()
            dismiss()
        }
        return timePickerDialog
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
    }
}

class Duration_Dialogs: DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pref : SharedPreferences =requireActivity().getSharedPreferences("saved_duration",
            Context.MODE_PRIVATE)
        val view = requireActivity().layoutInflater.inflate(
            R.layout.text_dialog, null
        )
        val edit = view.findViewById<TextView>(R.id.text_dialog)
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Duration")
                .setView(view)
            .setPositiveButton("OK") { _, _ ->
                pref.edit().putInt("saved_duration",
                    edit.text.toString().toInt()).apply()
                dismiss()
            }
            .setNegativeButton("CANCEL") { _, _ ->
                dismiss()
            }

        return builder.create()
    }
}
class Distance_Dialogs: DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pref : SharedPreferences =requireActivity().getSharedPreferences("saved_distance",
            Context.MODE_PRIVATE)
        val view = requireActivity().layoutInflater.inflate(
            R.layout.text_dialog, null
        )
        val edit = view.findViewById<TextView>(R.id.text_dialog)
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Distance")
                .setView(view)
            .setPositiveButton("OK") { _, _ ->
                pref.edit().putFloat("saved_distance",
                edit.text.toString().toFloat()).apply()
                dismiss()
            }
            .setNegativeButton("CANCEL") { _, _ ->
                dismiss()
            }

        return builder.create()
    }
}
class Calories_Dialogs: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pref : SharedPreferences =requireActivity().getSharedPreferences("saved_calorie",
            Context.MODE_PRIVATE)
        val view = requireActivity().layoutInflater.inflate(
            R.layout.text_dialog, null
        )
        val edit = view.findViewById<TextView>(R.id.text_dialog)
        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle("Calories")
            .setPositiveButton("OK") { _, _ ->
                pref.edit().putFloat("saved_calorie",
                    edit.text.toString().toFloat()).apply()
                dismiss()
            }
            .setNegativeButton("CANCEL") { _, _ ->
                dismiss()
            }

        return builder.create()
    }
}

class HeartRate_Dialogs: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pref : SharedPreferences =requireActivity().getSharedPreferences("saved_heartrate",
            Context.MODE_PRIVATE)
        val view = requireActivity().layoutInflater.inflate(
            R.layout.text_dialog, null
        )
        val edit = view.findViewById<TextView>(R.id.text_dialog)
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Heart Rate")
                .setView(view)
            .setPositiveButton("OK") { _, _ ->
                pref.edit().putInt("saved_heartrate",
                    edit.toString().toInt()).apply()
                dismiss()
            }
            .setNegativeButton("CANCEL") { _, _ ->
                dismiss()
            }

        return builder.create()
    }
}
class Manual_Comment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(
            R.layout.text_dialog, null
        )
        val edit = view.findViewById<TextView>(R.id.text_dialog)
        edit.inputType = InputType.TYPE_CLASS_TEXT
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Comment")
                .setView(view)
            .setPositiveButton("OK") { _, _ ->
                dismiss()
            }
            .setNegativeButton("CANCEL") { _, _ ->
                dismiss()
            }

        return builder.create()
    }
}

class Photo_Change(private val act: ProfileActivity?=null): DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Pick Profile Picture")
            .setPositiveButton("Open Camera") { _, _ ->
                dismiss()
                val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                act!!
                intent.putExtra(MediaStore.EXTRA_OUTPUT, act.temp_img_uri)
                act.cameraResult.launch(intent)
            }
            .setNegativeButton("Select from Gallery") { _, _ ->
                dismiss()
                act!!
                val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                act.pickImage.launch(intent)
            }

        return builder.create()
    }

}