package ipren.watchr.TestUtil;


import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTestUtil {


    // Used when we want to return the first LiveData onChanged value
    public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        return getVal(liveData, 0);
    }

    // Used when we don't want to return the first LiveData onChanged value
    // for example when we might be trying to get something from the api, the movie repository will return
    // null first which we don't want the value of
    public static <T> T getValue(final LiveData<T> liveData, int timeout) throws InterruptedException {
        return getVal(liveData, timeout);
    }


    // Get a value from a LiveData object
    private static <T> T getVal(final LiveData<T> liveData, int timeout) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        boolean timeoutSet = timeout != 0;
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                // Don't stop observing if we have our own timeout
                if (!timeoutSet) {
                    latch.countDown();
                    liveData.removeObserver(this);
                }
            }
        };
        liveData.observeForever(observer);
        if (timeoutSet)
            latch.await(timeout, TimeUnit.SECONDS);
        else
            latch.await(3, TimeUnit.SECONDS);
        return (T) data[0];
    }
}
