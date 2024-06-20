package workers.DomainLayer;

import java.util.Map;

public class BranchManager {
    public static Map<String, Integer> getMinimalWorkersForShift(Branch branch) {
        return ((HeadOfBranch)branch.getHeadOfBranch()).getMinimalWorkersForShift();
    }
}
