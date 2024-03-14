package com.kta.app.home

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kta.app.R
import com.kta.app.data.Schedule
import com.kta.app.databinding.FragmentHomeBinding
import com.kta.app.schedule.DetailScheduleFragment
import com.kta.app.utils.EncryptPreferences
import com.kta.app.utils.expired
import com.kta.app.utils.loadSvg

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private val handler = Handler(Looper.getMainLooper())
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvHomeSchedule
        adapter = HomeAdapter(
            onClick = { showScheduleDetail(it) }
        )
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }

        userProfile()
        getTodaySch()
        refresh()
        download()
    }

    private fun refresh() {
        binding.refresh.setOnClickListener {
            binding.refresh.visibility = View.GONE
            userProfile()
            getTodaySch()
            handler.postDelayed({
                binding.refresh.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    getString(R.string.refreshSuccess),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }, 3000.toLong())
        }
    }

    private fun userProfile() {
        val preference = EncryptPreferences(requireContext())
        val token = preference.getPreferences().getString("token", null)

        viewModel.userProfile(token.toString(),
            onSuccess = {
                val imageUrl = it.ktaUrl
                binding.apply {
                    homeName.text = it.name
                    homeRole.text = it.role
                    Glide.with(requireContext())
                        .load(it.imageProfile)
                        .circleCrop()
                        .into(binding.homeImage)
                }
                loadSvg(imageUrl, 300, 200) {
                    requireActivity().runOnUiThread {
                        if (it != null) {
                            binding.imgKTA.setImageDrawable(it)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.imageFailed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            },
            onFailure = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                expired(it, requireContext())
            },
            loading = {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            })
    }

    private fun getTodaySch() {
        val preference = EncryptPreferences(requireContext())
        val token = preference.getPreferences().getString("token", null)

        viewModel.getTodaySch(token.toString(),
            onSuccess = {
                val pagingData: PagingData<Schedule> = PagingData.from(it)
                adapter.submitData(lifecycle, pagingData)
                recyclerView.layoutManager?.scrollToPosition(0)
            },
            onFailure = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                expired(it, requireContext())
            },
            loading = {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            })
    }

    private fun download() {
        binding.download.setOnClickListener {
            try {
                val downloadManager =
                    requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
                if (downloadManager != null) {
                    val fileUrl =
                        "https://pslb3.menlhk.go.id/internal/uploads/pengumuman/1545111808_contoh-pdf.pdf"
                    val fileName = "E-KTA.pdf"
                    val request = DownloadManager.Request(Uri.parse(fileUrl))
                        .setTitle("Download File")
                        .setDescription("Download KTA")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            fileName
                        )
                    downloadManager.enqueue(request)
                    Toast.makeText(requireContext(), "Berhasil Diunduh", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Pengelola Unduhan tidak tersedia",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Terjadi kesalahan saat mengunduh file",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }

    private fun showScheduleDetail(schedule: Schedule) {
        val fragment = DetailScheduleFragment.newInstance(schedule)
        (requireActivity() as AppCompatActivity).supportFragmentManager
            .beginTransaction().apply {
                setCustomAnimations(R.anim.slide_up, 0, 0, 0)
                replace(R.id.fragment_container, fragment)
                addToBackStack(null)
                commit()
            }

        val loginFrame = fragment.view?.findViewById<LinearLayout>(R.id.detailFrame)
        loginFrame?.let { view ->
            view.alpha = 0f
            view.animate()
                .alpha(1f)
                .setDuration(500)
                .start()
        }
    }
}
