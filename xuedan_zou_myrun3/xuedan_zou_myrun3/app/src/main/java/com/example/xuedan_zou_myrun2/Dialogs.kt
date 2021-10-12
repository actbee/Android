package com.example.xuedan_zou_myrun2

import android.widget.Toast
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.provider.MediaStore
import android.text.InputType
import android.widget.*
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
            "unit_saved",
            Context.MODE_PRIVATE
        )
        val view  = requireActivity().layoutInflater.inflate(R.layout.unit_dialog, null)
        val rg = view.findViewById<RadioGroup>(R.id.UnitPreferences)
        rg.check(pref.getInt("unit", 0))
        rg.setOnCheckedChangeListener { _, which ->
            pref.edit().putInt("unit", which).apply()
            Toast.makeText(requireActivity(),"get!"+ which.toString(), Toast.LENGTH_SHORT).show()
        }

        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Unit Preference")
            .setView(view)
            .setNegativeButton("CANCEL"){ _, _->
                dismiss()
            }

        return builder.create()
    }
}

/*
class Date_Dialogs: DialogFragment(), DatePickerDialog.OnDateSetListener {
    val calendar = Calendar.getInstance()
    var date:String = "noonnn"
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        val pref : SharedPreferences =requireActivity().getSharedPreferences("start",
            Context.MODE_PRIVATE)

        val dateListener = DatePickerDialog.OnDateSetListener{ _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            date = year.toString()+month.toString()+day.toString()
        }

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
               calendar.get(Calendar.MONTH).toString()
                        +calendar.get(Calendar.DAY_OF_MONTH).toString()+calendar.get(Calendar.YEAR).toString()
            ).apply()
           // Toast.makeText(requireActivity(),calendar.get(Calendar.MONTH).toString()
           //         +calendar.get(Calendar.DAY_OF_MONTH).toString()+calendar.get(Calendar.YEAR).toString(),Toast.LENGTH_SHORT).show()

            Toast.makeText(requireActivity(),date,Toast.LENGTH_SHORT).show()
            dismiss()
        }
        return datePickerDialog
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
    }
}

 */

// modified by TA: LIU
class Date_Dialogs : DialogFragment(),DatePickerDialog.OnDateSetListener{
    private lateinit var calendar: Calendar
    private var date = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calendar = Calendar.getInstance()
        val pref : SharedPreferences =requireActivity().getSharedPreferences("start",
            Context.MODE_PRIVATE)

        val dateListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                date = String.format("%02d", month + 1) + "-" +
                        String.format("%02d", dayOfMonth)+ "-" +
                        String.format("%d", year)
                // println(date)
                Toast.makeText(requireActivity(),date,Toast.LENGTH_SHORT).show()
                pref.edit().putString("saved_date",date).apply()
            }

        val datePickerDialog = DatePickerDialog(
            requireActivity(), dateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        return datePickerDialog
    }
    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }
}

class MyTimeDialog :DialogFragment(), TimePickerDialog.OnTimeSetListener{
    private lateinit var calendar: Calendar
    private var time = ""
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calendar = Calendar.getInstance()
        val pref : SharedPreferences =requireActivity().getSharedPreferences("start",
            Context.MODE_PRIVATE)

        val timeListener =
            TimePickerDialog.OnTimeSetListener { timePicker, hour, miniute ->
                val second = (0..60).random()
                time = String.format("%02d", hour) + "-" +
                        String.format("%02d", miniute ) + "-" +
                        String.format("%02d", second)
                //  println(time)
                Toast.makeText(requireActivity(),time,Toast.LENGTH_SHORT).show()
                pref.edit().putString("saved_time",time).apply()
            }

        val timePickerDialog = TimePickerDialog(
            requireActivity(), timeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        )

        return timePickerDialog
    }
    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}

/*
class MyTimeDialog: DialogFragment(), TimePickerDialog.OnTimeSetListener{
    val calendar = Calendar.getInstance()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pref : SharedPreferences =requireActivity().getSharedPreferences("start",
            Context.MODE_PRIVATE)
        val timePickerDialog = TimePickerDialog(
            requireContext(), this,
            calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        )
        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel") { _, _ ->
            dismiss()
        }
        timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok") { _, _ ->
            pref.edit().putString("saved_time",
                 Calendar.HOUR_OF_DAY.toString()+Calendar.MINUTE.toString()).apply()
            dismiss()
        }
        return timePickerDialog
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
    }
}
 */

class Duration_Dialogs: DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pref : SharedPreferences =requireActivity().getSharedPreferences("start",
            Context.MODE_PRIVATE)
        val view = requireActivity().layoutInflater.inflate(
            R.layout.text_dialog, null
        )
        val edit = view.findViewById<TextView>(R.id.text_dialog)
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Duration")
                .setView(view)
            .setPositiveButton("OK") { _, _ ->
                if(!edit.text.toString().isBlank()) {
                    pref.edit().putInt(
                        "saved_duration",
                        edit.text.toString().toInt()
                    ).apply()
                }
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
        val pref : SharedPreferences =requireActivity().getSharedPreferences("start",
            Context.MODE_PRIVATE)
        val pref_unit: SharedPreferences = requireActivity().getSharedPreferences(
            "unit_saved",
            Context.MODE_PRIVATE
        )
        val view = requireActivity().layoutInflater.inflate(
            R.layout.text_dialog, null
        )
        val edit = view.findViewById<TextView>(R.id.text_dialog)
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Distance")
                .setView(view)
            .setPositiveButton("OK") { _, _ ->
                val unit = pref_unit.getInt("unit", 0)
               // Toast.makeText(requireActivity(),"get!"+ unit.toString(), Toast.LENGTH_SHORT).show()
                if(!edit.text.toString().isBlank()) {
                    when(unit){
                        2131296542 ->{
                            // just save them as kl in database
                            pref.edit().putFloat(
                                "saved_distance",
                                edit.text.toString().toFloat()
                            ).apply()
                        }
                        else ->{
                            // transfer m to kl and save them in database
                            pref.edit().putFloat(
                                "saved_distance",
                                edit.text.toString().toFloat() /0.621371F
                            ).apply()
                        }
                    }
                    dismiss()
                }
            }
            .setNegativeButton("CANCEL") { _, _ ->
                dismiss()
            }

        return builder.create()
    }
}
class Calories_Dialogs: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pref : SharedPreferences =requireActivity().getSharedPreferences("start",
            Context.MODE_PRIVATE)
        val view = requireActivity().layoutInflater.inflate(
            R.layout.text_dialog, null
        )
        val edit = view.findViewById<TextView>(R.id.text_dialog)
        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle("Calories")
            .setPositiveButton("OK") { _, _ ->
                if(!edit.text.toString().isBlank()) {
                    pref.edit().putFloat(
                        "saved_calorie",
                        edit.text.toString().toFloat()
                    ).apply()
                    dismiss()
                }
            }
            .setNegativeButton("CANCEL") { _, _ ->
                dismiss()
            }

        return builder.create()
    }
}

class HeartRate_Dialogs: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val pref : SharedPreferences =requireActivity().getSharedPreferences("start",
            Context.MODE_PRIVATE)
        val view = requireActivity().layoutInflater.inflate(
            R.layout.text_dialog, null
        )
        val edit = view.findViewById<TextView>(R.id.text_dialog)
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Heart Rate")
                .setView(view)
            .setPositiveButton("OK") { _, _ ->
                if (!edit.text.toString().isBlank()) {
                pref.edit().putInt(
                    "saved_heartrate",
                    edit.text.toString().toInt()
                ).apply()
                dismiss()
            }
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