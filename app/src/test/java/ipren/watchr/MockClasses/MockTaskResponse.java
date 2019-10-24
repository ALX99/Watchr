package ipren.watchr.MockClasses;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;
//TODO remake this test
public class MockTaskResponse extends Task {
    boolean isComplete = false;
    boolean isSuccessful = false;
    boolean isCanceled = false;
    Object result = null;
    Exception exception = null;

    public MockTaskResponse setComplete(boolean complete){
        this.isComplete = complete;
        return this;
    }

    public MockTaskResponse setSuccessful(boolean successful){
        this.isSuccessful = successful;
        return this;

    }
    public MockTaskResponse setCanceled(boolean canceled){
        this.isCanceled = canceled;
        return this;
    }

    public MockTaskResponse setResult(Object result){
        this.result = result;
        return this;
    }

    public MockTaskResponse setException(Exception exception){
        this.exception = exception;
        return this;
    }


    @NonNull
    @Override
    public Task addOnCompleteListener(@NonNull OnCompleteListener onCompleteListener) {
        onCompleteListener.onComplete(this);
        return this;
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public boolean isSuccessful() {
        return isSuccessful;
    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }

    @Nullable
    @Override
    public Object getResult() {
        return result;
    }

    @Nullable
    @Override
    public Exception getException() {
        return exception;
    }

    @NonNull
    @Override
    public Task addOnSuccessListener(@NonNull OnSuccessListener onSuccessListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener onSuccessListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener onSuccessListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
        return null;
    }

    @Nullable
    @Override
    public Object getResult(@NonNull Class aClass) throws Throwable {
        return null;
    }
}
