package org.colocation.scheduler;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.colocation.ColocationHost;
import org.colocation.bestEffort.ColocationTask;
import org.workflowsim.scheduling.BaseSchedulingAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wkj on 2019/3/14.
 */
public class FCFSAlgorithm extends BaseSchedulingAlgorithm {
    public FCFSAlgorithm(){
        super();
        this.setCloudletList(new ArrayList<>());
        Log.printLine("create cpas scheduler");
    }
    /**
     * The main function
     *
     * @throws Exception
     */
    @Override
    public void run() throws Exception {
        // score
        // weight
        // remove success scheduled tasks
        List<ColocationHost> vmList = getVmList();
        List<ColocationTask> taskToRemove = new ArrayList<>();
        List<ColocationTask> tasks = this.getCloudletList();
        for(ColocationTask t : tasks) {
            Collections.shuffle(vmList);
            for (ColocationHost vm : vmList) {
                long avail = vm.getBeAvailableRam();
                if (avail >= t.getRamQuota()) {
                    Log.printLine(CloudSim.clock()+": schedule task #"+t.getTaskFullName()+" to vm:#"+vm.getId());
                    t.setVmId(vm.getId());
                    taskToRemove.add(t);
                    break;
                }
            }
        }
        for (ColocationTask t: taskToRemove) {
            tasks.remove(t);
        }
        this.setCloudletList(tasks);
    }
}
