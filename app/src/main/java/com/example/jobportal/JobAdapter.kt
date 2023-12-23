package com.example.jobportal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JobAdapter(
    private val onEditClick: (Job) -> Unit,
    private val onDeleteClick: (Job) -> Unit
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    private var jobs: List<Job> = listOf()

    fun setJobs(jobs: List<Job>) {
        this.jobs = jobs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.job_item, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]
        holder.bind(job)
    }

    override fun getItemCount(): Int {
        return jobs.size
    }

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.job_title)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.job_description)
        private val budgetTextView: TextView = itemView.findViewById(R.id.job_budget)
        private val contactNumberTextView: TextView = itemView.findViewById(R.id.job_contact_number)
        private val locationTextView: TextView = itemView.findViewById(R.id.job_location)
        private val editButton: ImageButton = itemView.findViewById(R.id.edit_button)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)

        fun bind(job: Job) {
            titleTextView.text = job.title
            descriptionTextView.text = job.description
            budgetTextView.text = itemView.context.getString(R.string.budget_format, job.budget)
            contactNumberTextView.text = job.contactNumber
            locationTextView.text = job.location

            editButton.setOnClickListener {
                onEditClick(job)
            }

            deleteButton.setOnClickListener {
                onDeleteClick(job)
            }
        }
    }
}
