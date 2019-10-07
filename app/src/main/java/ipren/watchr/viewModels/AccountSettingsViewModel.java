package ipren.watchr.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ipren.watchr.dataHolders.User;
import ipren.watchr.repository.IMainRepository;
import ipren.watchr.repository.MainRepository;


public class AccountSettingsViewModel extends ViewModel {

   IMainRepository mainRepository;

   LiveData<User> liveUser;

   public AccountSettingsViewModel(){
       mainRepository = MainRepository.getMainRepository();
       this.liveUser = mainRepository.getUserLiveData();

   }

   public LiveData<User> getUser(){
       return liveUser;
   }


}
