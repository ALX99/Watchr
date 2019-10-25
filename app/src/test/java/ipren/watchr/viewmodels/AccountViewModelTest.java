package ipren.watchr.viewmodels;

import androidx.lifecycle.LiveData;

import org.junit.Test;

import java.util.List;

import ipren.watchr.MockClasses.MovieRepositoryAdapter;
import ipren.watchr.MockClasses.UserDataRepositoryAdapter;
import ipren.watchr.dataholders.Comment;
import ipren.watchr.dataholders.Movie;
import ipren.watchr.dataholders.Rating;
import ipren.watchr.repository.IUserDataRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class AccountViewModelTest {
    private String USER_ID = "USER_ID";
    @Test (expected = MethodInvokedCallBack.class)
    public void signOut() {
        AccountViewModel accountViewModel = new AccountViewModel(new UserDataRepositoryAdapter(){
            @Override
            public void signOutUser() {
                throw new MethodInvokedCallBack();
            }
        }, null);

        accountViewModel.signOut();
    }

    @Test (expected = MethodInvokedCallBack.class)
    public void getRatingByUserId() {
        AccountViewModel accountViewModel = new AccountViewModel(new UserDataRepositoryAdapter(){
            @Override
            public LiveData<Rating[]> getRatings(String id, int searchMethod) {
                assertTrue(id.equals(USER_ID));
                assertTrue(searchMethod == IUserDataRepository.SEARCH_METHOD_USER_ID);
                throw new MethodInvokedCallBack();

            }
        }, null);
        accountViewModel.getRatingByUserId(USER_ID);
    }

    @Test (expected = MethodInvokedCallBack.class)
    public void getCommentsByUserId() {
        AccountViewModel accountViewModel = new AccountViewModel(new UserDataRepositoryAdapter(){
            @Override
            public LiveData<Comment[]> getComments(String id, int searchMethod) {
                assertTrue(id.equals(USER_ID));
                assertTrue(searchMethod == IUserDataRepository.SEARCH_METHOD_USER_ID);
                throw new MethodInvokedCallBack();
            }
        }, null);

        accountViewModel.getCommentsByUserId(USER_ID);
    }

    @Test (expected = MethodInvokedCallBack.class)
    public void getFavoritesList() {
        AccountViewModel accountViewModel = new AccountViewModel(new UserDataRepositoryAdapter(){
            @Override
            public LiveData<String[]> getMovieList(String list, String id) {
                assertTrue(id.equals(USER_ID));
                assertEquals(IUserDataRepository.FAVORITES_LIST, list);
                throw new MethodInvokedCallBack();
            }
        }, null);

        accountViewModel.getFavoritesList(USER_ID);
    }

    @Test (expected = MethodInvokedCallBack.class)
    public void getWatchedList() {
        AccountViewModel accountViewModel = new AccountViewModel(new UserDataRepositoryAdapter(){
            @Override
            public LiveData<String[]> getMovieList(String list, String id) {
                assertTrue(id.equals(USER_ID));
                assertEquals(IUserDataRepository.WATCHED_LIST, list);
                throw new MethodInvokedCallBack();
            }
        }, null);

        accountViewModel.getWatchedList(USER_ID);
    }

    @Test (expected = MethodInvokedCallBack.class)
    public void getWatchLaterList() {
        AccountViewModel accountViewModel = new AccountViewModel(new UserDataRepositoryAdapter(){
            @Override
            public LiveData<String[]> getMovieList(String list, String id) {
                assertTrue(id.equals(USER_ID));
                assertEquals(IUserDataRepository.WATCH_LATER_LIST, list);
                throw new MethodInvokedCallBack();
            }
        }, null);
        accountViewModel.getWatchLaterList(USER_ID);
    }

    @Test (expected = MethodInvokedCallBack.class)
    public void getMovies() {
        final int[] ids = {1,2};
        AccountViewModel accountViewModel = new AccountViewModel(new UserDataRepositoryAdapter(), new MovieRepositoryAdapter(){
            @Override
            public LiveData<List<Movie>> getMoviesByID(int[] IDs) {
                assertTrue(IDs[0] == 1 && IDs[1] == 2);
                throw new MethodInvokedCallBack();
            }
        });

        accountViewModel.getMovies(ids);
    }


    private class MethodInvokedCallBack extends Error{}
}