package workers.DomainLayer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShiftManager {
    public static boolean notIn(Shift shift,Worker worker) {
        for (Map.Entry<String, List<Worker>> entry : shift.getWorkers().entrySet()) {
            if(entry.getValue().contains(worker)) {
                return false;
            }
        }
        return true;
    }
    public static void altarRole(Shift shift,int id, String role) {
        Map<String,List<Worker>> workers = shift.getWorkers();
        for (Map.Entry<String, List<Worker>> entry : workers.entrySet()) {
            for (Worker worker : entry.getValue()) {
                if(worker.getID() == id) {
                    entry.getValue().remove(worker);
                    if(workers.containsKey(role)) {
                        workers.get(role).add(worker);
                    }
                    else {
                        workers.put(role,new LinkedList<>());
                        workers.get(role).add(worker);
                    }
                    return;
                }
            }
        }
    }
    public static boolean isWorkerInShift(Shift shift,int id) {
        Map<String,List<Worker>> workers = shift.getWorkers();
        for (Map.Entry<String, List<Worker>> entry : workers.entrySet()) {
            for (Worker worker : entry.getValue()) {
                if(worker.getID() == id) {
                    return true;
                }
            }
        }
        return false;
    }
}
