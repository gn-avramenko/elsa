/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.lock;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.model.common.Xeption;
import com.gridnine.elsa.common.core.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Component
public class LockTemplate {

    @Autowired
    private LockManager lm;

    public <T> T withLock(Object obj, Long tryTime, TimeUnit timeUnit, Callable<T> body)  {
        return ExceptionUtils.wrapException(() ->{
            var lockName = getLockName(obj);
            try(var lock = lm.getLock(lockName)){
                if(!lock.tryLock(tryTime, timeUnit)){
                    throw Xeption.forDeveloper("unable to get lock %s during %s %s".formatted(lockName, tryTime, timeUnit));
                }
                try{
                    return body.call();
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    public <T> T withLock(Object obj, Callable<T> body){
        return withLock(obj, 1L, TimeUnit.MINUTES, body);
    }


    private static String getLockName(Object obj) {
        if (obj instanceof BaseIdentity) {
            return "%s-%s".formatted(obj.getClass().getName(), ((BaseIdentity) obj).getId());
        }
        if (obj instanceof String) {
            return obj.toString();
        }
        return "%s-%s".formatted(obj.getClass().getName(), obj.hashCode());
    }
//    fun<T:Any> withLock(obj:Any, tryTime:Long, timeUnit:TimeUnit, function:()->T):T{
//        val lockName = getLockName(obj)
//        lateinit var result:T
//        LockManager.get().getLock(lockName).use {
//            if(!it.tryLock(tryTime, timeUnit)){
//                throw Xeption.forDeveloper("unable to get lock $lockName during $tryTime $timeUnit")
//            }
//            try{
//                result = function.invoke()
//            } finally {
//                it.unlock()
//            }
//        }
//        return result
//    }
//
//    fun<T:Any> withLock(obj:Any, function:()->T):T{
//        return withLock(obj, 1, TimeUnit.MINUTES, function)
//    }
//
//    private fun getLockName(obj:Any):String{
//        if(obj is BaseIdentity){
//            return "${obj::class.qualifiedName}-${obj.uid}"
//        }
//        if(obj is String){
//            return obj
//        }
//        return "${obj::class.qualifiedName}-${obj.hashCode()}"
//    }

}
