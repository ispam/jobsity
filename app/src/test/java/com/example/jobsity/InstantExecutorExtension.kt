package com.example.jobsity

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class InstantExecutorExtension: BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance()
            .setDelegate(object : TaskExecutor(){
                override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
                override fun executeOnMainThread(runnable: Runnable) = runnable.run()
                override fun isMainThread(): Boolean = true
                override fun postToMainThread(runnable: Runnable) {
                    // no op
                }
            })
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}