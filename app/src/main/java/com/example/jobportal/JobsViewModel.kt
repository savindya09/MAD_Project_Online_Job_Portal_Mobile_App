package com.example.jobportal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class JobsViewModel : ViewModel() {

    private val _jobs = MutableLiveData<List<Job>>()
    val jobs: LiveData<List<Job>> = _jobs

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance("https://jobportal-1a523-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("jobs")

    init {
        fetchJobs()
    }

    private fun fetchJobs() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jobsList = mutableListOf<Job>()
                for (jobSnapshot in snapshot.children) {
                    if (jobSnapshot.value is HashMap<*, *>) {
                        val jobMap = jobSnapshot.value as HashMap<*, *>
                        val job = Job(
                            jobSnapshot.key.toString(),
                            jobMap["title"].toString(),
                            jobMap["description"].toString(),
                            (jobMap["budget"] as Number).toDouble(),
                            jobMap["contactNumber"].toString(),
                            jobMap["location"].toString()
                        )
                        jobsList.add(job)
                    }
                }
                _jobs.value = jobsList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun addJob(job: Job) {
        databaseReference.push().setValue(job)
    }

    fun updateJob(job: Job) = viewModelScope.launch {
        databaseReference.child(job.id).setValue(job)
    }

    fun deleteJob(job: Job) = viewModelScope.launch {
        databaseReference.child(job.id).removeValue()
    }

    fun getAllJobs(): LiveData<List<Job>> {
        return jobs
    }
}
