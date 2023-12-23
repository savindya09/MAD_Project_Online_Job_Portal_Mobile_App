package com.example.jobportal

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: JobsViewModel
    private lateinit var jobAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(JobsViewModel::class.java)

        jobAdapter = JobAdapter(
            onEditClick = { job -> editJob(job) },
            onDeleteClick = { job -> viewModel.deleteJob(job) }
        )

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = jobAdapter

        viewModel.jobs.observe(this, Observer<List<Job>> { jobs ->
            jobAdapter.setJobs(jobs)
        })

        val fabAddJob: FloatingActionButton = findViewById(R.id.fab_add_job)
        fabAddJob.setOnClickListener {
            showAddEditJobDialog()
        }
    }

    private fun editJob(job: Job) {
        showAddEditJobDialog(job)
    }

    private fun showAddEditJobDialog(jobToEdit: Job? = null) {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.add_job_dialog, null)

        val titleEditText = view.findViewById<EditText>(R.id.title_edit_text)
        val descriptionEditText = view.findViewById<EditText>(R.id.description_edit_text)
        val budgetEditText = view.findViewById<EditText>(R.id.budget_edit_text)
        val contactNumberEditText = view.findViewById<EditText>(R.id.contact_number_edit_text)
        val locationEditText = view.findViewById<EditText>(R.id.location_edit_text)

        // If we are editing a job, pre-fill the input fields
        if (jobToEdit != null) {
            titleEditText.setText(jobToEdit.title)
            descriptionEditText.setText(jobToEdit.description)
            budgetEditText.setText(jobToEdit.budget.toString())
            contactNumberEditText.setText(jobToEdit.contactNumber)
            locationEditText.setText(jobToEdit.location)
        }

        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        builder.setTitle(if (jobToEdit == null) getString(R.string.add_job) else getString(R.string.edit_job))
        builder.setPositiveButton(if (jobToEdit == null) getString(R.string.add) else getString(R.string.update)) { dialog, _ ->
            val job = Job(
                id = jobToEdit?.id ?: "",
                title = titleEditText.text.toString(),
                description = descriptionEditText.text.toString(),
                budget = budgetEditText.text.toString().toDoubleOrNull() ?: 0.0,
                contactNumber = contactNumberEditText.text.toString(),
                location = locationEditText.text.toString()
            )

            if (jobToEdit == null) {
                viewModel.addJob(job)
            } else {
                viewModel.updateJob(job)
            }

            // Clear the input fields
            titleEditText.text.clear()
            descriptionEditText.text.clear()
            budgetEditText.text.clear()
            contactNumberEditText.text.clear()
            locationEditText.text.clear()

            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

//    private fun showAddJobDialog() {
//        val inflater = LayoutInflater.from(this)
//        val view = inflater.inflate(R.layout.add_job_dialog, null)
//
//        val titleEditText = view.findViewById<EditText>(R.id.title_edit_text)
//        val descriptionEditText = view.findViewById<EditText>(R.id.description_edit_text)
//        val budgetEditText = view.findViewById<EditText>(R.id.budget_edit_text)
//        val contactNumberEditText = view.findViewById<EditText>(R.id.contact_number_edit_text)
//        val locationEditText = view.findViewById<EditText>(R.id.location_edit_text)
//
//        val builder = AlertDialog.Builder(this)
//        builder.setView(view)
//        builder.setTitle(getString(R.string.add_job))
//        builder.setPositiveButton(getString(R.string.add)) { dialog, _ ->
//            val job = Job(
//                title = titleEditText.text.toString(),
//                description = descriptionEditText.text.toString(),
//                budget = budgetEditText.text.toString().toDoubleOrNull() ?: 0.0,
//                contactNumber = contactNumberEditText.text.toString(),
//                location = locationEditText.text.toString()
//            )
//
//            viewModel.addJob(job)
//
//            // Clear the input fields
//            titleEditText.text.clear()
//            descriptionEditText.text.clear()
//            budgetEditText.text.clear()
//            contactNumberEditText.text.clear()
//            locationEditText.text.clear()
//
//            dialog.dismiss()
//        }
//        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
//            dialog.dismiss()
//        }
//
//        val alertDialog = builder.create()
//        alertDialog.show()
//    }
}