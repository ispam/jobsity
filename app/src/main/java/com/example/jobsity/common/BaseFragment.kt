package com.example.jobsity.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.jobsity.R
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding: VB get() = _binding!!

    private var dialog: Dialog? = null

    abstract fun onViewCreated()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod(
            INFLATE_KEY,
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        _binding = method.invoke(null, layoutInflater, container, false) as VB
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }

    override fun onDestroyView() {
        closeDialog()
        _binding = null
        super.onDestroyView()
    }

    fun showDialog(
        context: Context,
        title: String = getString(R.string.dialog_loading),
        isCancelable: Boolean = false
    ) {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.base_dialog_view, null)
        if (title != null) {
            view.findViewById<TextView>(R.id.message).apply {
                text = title
            }
        }
        closeDialog()
        dialog = Dialog(context).apply {
            setContentView(view)
            setCancelable(isCancelable)
            setCanceledOnTouchOutside(isCancelable)
            show()
        }
    }

    fun delayedBlock(delayMillis: Long = 250L, block: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            block.invoke()
        }, delayMillis)
    }

    fun closeDialog() {
        dialog?.dismiss()
    }

    companion object {
        private const val INFLATE_KEY = "inflate"
    }
}