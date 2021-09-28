package com.example.xuedan_zou_myrun2

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment

class UnitDialog : DialogFragment(){
    companion object{
        const val DIALOG_KEY="dialog"
        const val UNIT_DIALOG=1
        const val COMMENT_DIALOG=2
    }



    override fun onCreateDialog(savedInstanceState : Bundle?): Dialog{
        lateinit var ret_dialog: Dialog
        val bundle=arguments
        val dialog_ID=bundle?.getInt(DIALOG_KEY)
        if(dialog_ID== UNIT_DIALOG ){
            val builder = AlertDialog.Builder(requireActivity())
            val view: View = requireActivity().layoutInflater.inflate(R.layout.unit_dialog, null)
            builder.setView(view)
            builder.setTitle("Unit Preference")
            ret_dialog = builder.create()
        }
        else if(dialog_ID==COMMENT_DIALOG){
            val builder = AlertDialog.Builder(requireActivity())
            val view: View = requireActivity().layoutInflater.inflate(R.layout.comment_dialog, null)
            builder.setView(view)
            builder.setTitle("Comment")
            ret_dialog = builder.create()
        }
        return ret_dialog
    }

}