package fatcat.snowberry.core;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;

import fatcat.snowberry.dp.DesignPatternCore;
import fatcat.snowberry.tag.TagCore;


public class SnowberryLauncher implements IStartup {

	@Override
	public void earlyStartup() {
		// 启动部件
		Job job = new Job(International.SnowberryLauncher) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				TagCore.start();
				DesignPatternCore.start();
				monitor.done();  
	            return Status.OK_STATUS;
			}
		};
		job.schedule();
	}
	
}
