package com.example.location.reminder.reminders.reminderslist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import com.example.location.reminder.R
import com.example.location.reminder.app.utils.ResponseResult
import com.example.location.reminder.base.BaseFragment
import com.example.location.reminder.base.NavigationCommand
import com.example.location.reminder.databinding.FragmentRemindersBinding
import com.example.location.reminder.app.utils.setDisplayHomeAsUpEnabled
import com.example.location.reminder.app.utils.setTitle
import com.example.location.reminder.app.utils.setup
import com.example.location.reminder.authentication.ui.activity.AuthenticationActivity
import com.example.location.reminder.authentication.ui.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderListFragment : BaseFragment() {
    //use Koin to retrieve the ViewModel instance
    override val _viewModel: RemindersListViewModel by viewModel()
    private val authViewModel : AuthViewModel by activityViewModel()

    private lateinit var binding: FragmentRemindersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemindersBinding.inflate(layoutInflater, container, false)
        binding.viewModel = _viewModel

        // menu
        val menuHost : MenuHost = requireActivity()
        initMenu(menuHost)

        setDisplayHomeAsUpEnabled(false)
        setTitle(getString(R.string.app_name))

        binding.refreshLayout.setOnRefreshListener { _viewModel.loadReminders() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()

        binding.addReminderFAB.setOnClickListener {
            navigateToAddReminder()
        }

        authViewModel.loginGoogleAccountState.observe(viewLifecycleOwner) { result ->
            Log.d("ReminderListFragment", "==> Observing")
            when (result) {
                is ResponseResult.Error -> Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                is ResponseResult.Loading -> Toast.makeText(requireContext(), "Loading..", Toast.LENGTH_SHORT).show()
                is ResponseResult.Success -> Toast.makeText(requireContext(), "done", Toast.LENGTH_SHORT).show()
                null -> Log.d("ReminderListFragment", "Null")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //load the reminders list on the ui
        _viewModel.loadReminders()
    }

    private fun navigateToAddReminder() {
        // use the navigationCommand live data to navigate between the fragments
        _viewModel.navigationCommand.postValue(
            NavigationCommand.To(
                ReminderListFragmentDirections.toSaveReminder()
            )
        )
    }

    private fun setupRecyclerView() {
        val adapter = RemindersListAdapter {}

        binding.reminderssRecyclerView.setup(adapter)
    }


    private fun initMenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.logout -> {
                        authViewModel.signOut()
                        navigateToAuthenticationActivity()
                    }
                }
                return true
            }
        })
    }


    private fun navigateToAuthenticationActivity() {
        val intent = Intent(requireContext(), AuthenticationActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
