package com.example.smarthome.fragments

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.smarthome.R
import com.example.smarthome.databinding.FragmentUserBinding
import com.example.smarthome.homesection.SignUp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
import com.example.smarthome.R
import com.example.smarthome.databinding.FragmentUserBinding
import com.example.smarthome.auth.RegisterActivity

class UserFragment : Fragment() {
    var binding: FragmentUserBinding? = null
    var googleSignInClient: GoogleSignInClient? = null
    var auth: FirebaseAuth? = null

    var yr: String = ""
    var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //        Toast.makeText(getActivity(), "Something went wrong0", Toast.LENGTH_SHORT).show();
        requireActivity().window.statusBarColor = ContextCompat.getColor(context!!, R.color.white)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(activity!!, gso)
        auth = FirebaseAuth.getInstance()
        binding.signout.setOnClickListener { v -> signOutAndRevokeAccess() }


        //        SharedPreferences sharedPreferences=getContext().getSharedPreferences(
//                "save", MODE_PRIVATE);

//        binding.gender.setChecked(sharedPreferences.getBoolean("value",true));
//
//        if(binding.gender.isChecked())
//            binding.genderDisp.setText("Female");
//        else
//            binding.genderDisp.setText("Male");


//        binding.gender.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(binding.gender.isChecked()) {
//                    binding.genderDisp.setText("Female");
//                    SharedPreferences.Editor editor=view.getContext().getSharedPreferences("save",MODE_PRIVATE).edit();
//                    editor.putBoolean("value",true);
//                    editor.apply();
//                    binding.gender.setChecked(true);
//                }
//
//                else {
//                    binding.genderDisp.setText("Male");
//                    SharedPreferences.Editor editor=view.getContext().getSharedPreferences("save",MODE_PRIVATE).edit();
//                    editor.putBoolean("value",false);
//                    editor.apply();
//                    binding.gender.setChecked(false);
//
//                }
//            }
//        });
        val user = FirebaseAuth.getInstance().currentUser
        val id = user!!.uid
        val documentReference = firestore.collection("user").document(id)
        documentReference.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val url = documentSnapshot.getString("userProfile")
                Picasso.get().load(url).into(binding.profileImage)

                binding.fullName.setText(documentSnapshot.getString("userName"))
                // Toast.makeText(getActivity(), "Error2", Toast.LENGTH_SHORT).show();
                val nickname = documentSnapshot.getString("userName") + " "
                binding.name.setText("@" + nickname.substring(0, nickname.indexOf(" ")))










                binding.profileTxt.setOnClickListener(View.OnClickListener {
                    val intent =
                        Intent(activity, ProfileSection::class.java)
                    startActivity(intent)
                })

                binding.parentTxt.setOnClickListener(View.OnClickListener {
                    val intent =
                        Intent(activity, ParentsSection::class.java)
                    startActivity(intent)
                })

                binding.proctorTxt.setOnClickListener(View.OnClickListener {
                    val intent =
                        Intent(activity, ProctorSection::class.java)
                    startActivity(intent)
                })

                binding.cabTxt.setOnClickListener(View.OnClickListener {
                    val intent = Intent(activity, CabSection::class.java)
                    startActivity(intent)
                })

                binding.sosTxt.setOnClickListener(View.OnClickListener {
                    val intent =
                        Intent(activity, MapsActivitySos::class.java)
                    startActivity(intent)
                })
                binding.aboutTxt.setOnClickListener(View.OnClickListener {
                    val intent =
                        Intent(activity, AboutSection::class.java)
                    startActivity(intent)
                })
                binding.emergencyBtn.setOnClickListener(View.OnClickListener {
                    val inflater = LayoutInflater.from(context)
                    val bottomSheetView: View =
                        inflater.inflate(R.layout.emergency_bottom_sheet, null)

                    // Create BottomSheetDialog
                    val bottomSheetDialog = BottomSheetDialog(context!!)
                    bottomSheetDialog.setContentView(bottomSheetView)

                    // Show the bottom sheet
                    bottomSheetDialog.show()


                    val police = bottomSheetView.findViewById<LinearLayout>(R.id.police)
                    val women = bottomSheetView.findViewById<LinearLayout>(R.id.women_helpline)
                    val ambulance = bottomSheetView.findViewById<LinearLayout>(R.id.ambulance)
                    val fire = bottomSheetView.findViewById<LinearLayout>(R.id.fire_helpline)


                    police.setOnClickListener {
                        val intent =
                            Intent(Intent.ACTION_DIAL)
                        intent.setData(Uri.parse("tel:100"))
                        startActivity(intent)
                    }

                    women.setOnClickListener {
                        val intent =
                            Intent(Intent.ACTION_DIAL)
                        intent.setData(Uri.parse("tel:1090"))
                        startActivity(intent)
                    }

                    ambulance.setOnClickListener {
                        val intent =
                            Intent(Intent.ACTION_DIAL)
                        intent.setData(Uri.parse("tel:102"))
                        startActivity(intent)
                    }
                    fire.setOnClickListener {
                        val intent =
                            Intent(Intent.ACTION_DIAL)
                        intent.setData(Uri.parse("tel:101"))
                        startActivity(intent)
                    }
                })


                //                    if (!documentSnapshot.getString("userAge").equals(" "))
                //                        // Toast.makeText(getActivity(), "Error1", Toast.LENGTH_SHORT).show();
                //                        binding.age.setText(documentSnapshot.getString("userAge"));

                //                    if (!documentSnapshot.getString("userGender").equals(" "))
                //                    {binding.genderDisp.setText(documentSnapshot.getString("userGender"));}
                //                    //Toast.makeText(getActivity(), "Error3", Toast.LENGTH_SHORT).show();


                //                    if (!documentSnapshot.getString("userYear").equals(" ")) {
                //                        String yr_retrive = documentSnapshot.getString("userYear");
                //                        if (yr_retrive.equals("1st yr"))
                //                        {
                //                            binding.firstyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
                //                            binding.firstyr.setTextColor(getResources().getColor(R.color.black));
                //                        }
                //                        else if (yr_retrive.equals("2nd yr")) {
                //                            binding.secondyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
                //                            binding.secondyr.setTextColor(getResources().getColor(R.color.black));
                //
                //                        }
                //                        else if (yr_retrive.equals("3rd yr")) {
                //                            binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
                //                            binding.thirdyr.setTextColor(getResources().getColor(R.color.black));
                //
                //                        }
                //                        else
                //                        {
                //                            binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
                //                            binding.fourthyr.setTextColor(getResources().getColor(R.color.black));
                //
                //                        }
                //
                //                    }

                //                    if (!documentSnapshot.getString("userOrigin").equals(" "))
                //                        binding.placeOfOrigin.setText(documentSnapshot.getString("userOrigin"));
                //Toast.makeText(getActivity(), "Error4", Toast.LENGTH_SHORT).show();
            }
        }.addOnFailureListener { Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show() }


        //        binding.save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                user_details userDetails=new user_details();
//                if(binding.age.getText().toString().isEmpty())
//                    Toast.makeText(getActivity(), "Enter Age", Toast.LENGTH_SHORT).show();
//
//                else if(binding.genderDisp.getText().toString().isEmpty())
//                    Toast.makeText(getActivity(), "Select Gender", Toast.LENGTH_SHORT).show();
//                else if(yr.isEmpty())
//                    Toast.makeText(getActivity(), "Select Year", Toast.LENGTH_SHORT).show();
//                else if(binding.placeOfOrigin.getText().toString().isEmpty())
//                    Toast.makeText(getActivity(), "Select State", Toast.LENGTH_SHORT).show();
//                else {
//                    firestore.runTransaction(new Transaction.Function<Void>() {
//                                @Override
//                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                                    // DocumentSnapshot snapshot = transaction.get(sfDocRef);
//
//
//                                    transaction.update(documentReference, "userAge", binding.age.getText().toString());
//                                    transaction.update(documentReference, "userGender", binding.genderDisp.getText().toString());
//                                    transaction.update(documentReference, "userOrigin", binding.placeOfOrigin.getText().toString());
//                                    transaction.update(documentReference, "userYear", yr);
//
//                                    // Success
//                                    return null;
//                                }
//                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                }
//
//            }
//        });

//        binding.firstyr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                yr = (String) binding.firstyr.getText().toString();
//                binding.firstyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
//                binding.secondyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.firstyr.setTextColor(getResources().getColor(R.color.black));
//                binding.thirdyr.setTextColor(getResources().getColor(R.color.white));
//                binding.secondyr.setTextColor(getResources().getColor(R.color.white));
//                binding.fourthyr.setTextColor(getResources().getColor(R.color.white));
//
//            }
//        });

//        binding.secondyr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                yr = (String) binding.secondyr.getText().toString();
//                binding.firstyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.secondyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
//                binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.secondyr.setTextColor(getResources().getColor(R.color.black));
//                binding.thirdyr.setTextColor(getResources().getColor(R.color.white));
//                binding.fourthyr.setTextColor(getResources().getColor(R.color.white));
//                binding.firstyr.setTextColor(getResources().getColor(R.color.white));
//            }
//        });


//        binding.thirdyr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                yr = (String) binding.thirdyr.getText().toString();
//                binding.firstyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.secondyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
//                binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.thirdyr.setTextColor(getResources().getColor(R.color.black));
//                binding.fourthyr.setTextColor(getResources().getColor(R.color.white));
//                binding.secondyr.setTextColor(getResources().getColor(R.color.white));
//                binding.firstyr.setTextColor(getResources().getColor(R.color.white));
//            }
//        });


//        binding.fourthyr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                yr = (String) binding.fourthyr.getText().toString();
//                binding.firstyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.secondyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
//                binding.fourthyr.setTextColor(getResources().getColor(R.color.black));
//                binding.thirdyr.setTextColor(getResources().getColor(R.color.white));
//                binding.secondyr.setTextColor(getResources().getColor(R.color.white));
//                binding.firstyr.setTextColor(getResources().getColor(R.color.white));
//            }
//        });


//        String[] state= getResources().getStringArray(R.array.state);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_states, state);
//        binding.placeOfOrigin.setAdapter(arrayAdapter);
    }

    private fun signOutAndRevokeAccess() {
        // Sign out from Firebase Authentication
        FirebaseAuth.getInstance().signOut()

        // Sign out from Google Sign-In
        googleSignInClient!!.signOut().addOnCompleteListener(
            activity!!
        ) { task: Task<Void?>? ->
            // Optionally, revoke access to remove the user's permissions
            googleSignInClient!!.revokeAccess().addOnCompleteListener(
                activity!!
            ) { revokeTask: Task<Void?>? ->
                // After successful sign-out and revocation, navigate to the login/signup screen
                val intent = Intent(activity, RegisterActivity::class.java)
                startActivity(intent)
                activity!!.finish() // Close the current activity so user can't return to the signed-in state
            }
        }
    } //    private void signout() {
    //        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
    //            @Override
    //            public void onComplete(@NonNull Task<Void> task) {
    //                 startActivity(new Intent(getActivity(),SignUp.class));
    //
    //            }
    //        });
    //    }
}