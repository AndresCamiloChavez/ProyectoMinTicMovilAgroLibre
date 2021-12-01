package com.example.agrolibre.view.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.agrolibre.R
import com.example.agrolibre.view.ui.activities.LoginActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val userAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)
        val btnExit = view.findViewById<Button>(R.id.btnExit)
        val progress = view.findViewById<ProgressBar>(R.id.progressAdmin)
        val img = view.findViewById<ImageView>(R.id.imgAdmin)


        if(userAuth.currentUser!!.photoUrl != null){
            Glide.with(view.context).load(userAuth.currentUser!!.photoUrl).into(img)
        }else{
            Glide.with(view.context).load("https://cdn-icons-png.flaticon.com/512/149/149071.png").into(img)
        }

        view.findViewById<TextView>(R.id.tvNameAdimin).text = userAuth.currentUser!!.displayName

        progress.visibility = View.GONE
        btnEditProfile?.setOnClickListener{
            findNavController().navigate(R.id.adminDetailFragmentDialog, null)
        }
        btnExit.setOnClickListener{
            progress.visibility = View.VISIBLE
            btnExit.isEnabled = false
            AuthUI.getInstance().signOut(view.context).addOnSuccessListener {
                startActivity(Intent(view.context, LoginActivity::class.java))
                activity?.finish()

            }.addOnFailureListener{
                progress.visibility = View.GONE
                btnExit.isEnabled = true
                Toast.makeText(view.context, "Ocurrío un error ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdminFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}