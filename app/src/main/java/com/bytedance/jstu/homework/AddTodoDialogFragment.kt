package com.bytedance.jstu.homework

import android.app.ActionBar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.autofill.AutofillValue
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bytedance.jstu.homework.TodoDatabase.TodoDB
import com.bytedance.jstu.homework.TodoDatabase.TodoRoomDatabase
import com.bytedance.jstu.homework.databinding.FragmentAddTodoDialogBinding
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddTodoDialogFragment(var fragmentState: Int, todoDb: TodoDB?=null) : DialogFragment() {

    private val todoViewModel: TodoViewModel by activityViewModels()
    private var isDatePickerEnabled : Boolean = false

    companion object {
        val STATE_ADD = 1
        val STATE_EDIT = 2
        val STATE_VIEW = 3
        val STATE_NONE = 0

        val TAG = "add_to_dialog_fragment"
    }


    private var _binding: FragmentAddTodoDialogBinding? = null
    private var todo : TodoDB? = todoDb

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddTodoDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (fragmentState == STATE_EDIT || fragmentState == STATE_VIEW)
        {
            todo?.let { todo->
                binding.edittxtTitle.autofill(AutofillValue.forText(todo.title))
                binding.edittxtDetail.autofill(AutofillValue.forText(todo.detail))
                binding.edittxtDate.autofill(AutofillValue.forDate(todo.dueTime))
            }

            if (fragmentState == STATE_VIEW) {
                binding.edittxtTitle.isFocusable = false
                binding.edittxtDetail.isFocusable = false
                binding.edittxtDate.isFocusable = false

                binding.edittxtTitle.isCursorVisible = false
                binding.edittxtDetail.isCursorVisible = false
                binding.edittxtDate.isCursorVisible = false

                binding.edittxtTitle.isFocusableInTouchMode = false
                binding.edittxtDetail.isFocusableInTouchMode = false
                binding.edittxtDate.isFocusableInTouchMode = false

                binding.tvState.text = "View"

                isDatePickerEnabled = false
            }

        }

        if (fragmentState == STATE_EDIT) {
            binding.tvState.text = "Edit"
            isDatePickerEnabled = true
        }

        if (fragmentState == STATE_ADD) {
            binding.tvState.text = "Add"
            isDatePickerEnabled = true
        }

        binding.btnConfirm.setOnClickListener {
            val title:String = binding.edittxtTitle.text.toString()
            val date:Long = binding.edittxtDate.editDateMillis
            val detail:String = binding.edittxtDetail.text.toString()
            if (fragmentState == STATE_ADD)
                todoViewModel.insert(TodoDB(title = title, dueTime =date, detail = detail, id = 0, isDone = false))
            else if (fragmentState == STATE_EDIT) {
                todo?.let { it1->
                    todoViewModel.update(
                        TodoDB(
                            title = title,
                            dueTime = date,
                            detail = detail,
                            id = it1.id,
                            isDone = it1.isDone
                        )
                    )
                }
            }
            dismiss()
        }

        binding.todoImgDelete.setOnClickListener {
            if (fragmentState == STATE_EDIT || fragmentState == STATE_VIEW) {
                todo?.let { todoViewModel.delete(it.id) }
                dismiss()
            }
        }

        binding.todoImgEdit.setOnClickListener {
            if (fragmentState == STATE_VIEW) {
                binding.edittxtTitle.isFocusable = true
                binding.edittxtDetail.isFocusable = true

                binding.edittxtTitle.isCursorVisible = true
                binding.edittxtDetail.isCursorVisible = true

                binding.edittxtTitle.isFocusableInTouchMode = true
                binding.edittxtDetail.isFocusableInTouchMode = true

                binding.edittxtDate.isClickable = true
                fragmentState = STATE_EDIT

                isDatePickerEnabled = true
                binding.tvState.text = "Edit"
                view.invalidate()
            }
        }

        binding.edittxtDate.setOnClickListener {
            if (isDatePickerEnabled)
                DatePickerFragment().show(childFragmentManager,"datePicker")
        }

        view.invalidate()
    }

    override fun onResume() {
        super.onResume()
        val window:Window? = dialog?.window
        window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun autofillDate(date : Long) {
        binding.edittxtDate.autofill(AutofillValue.forDate(date))
    }
}