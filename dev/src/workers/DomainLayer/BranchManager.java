package src.workers.DomainLayer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BranchManager {
    public static Map<String, Integer> getMinimalWorkersForShift(Branch branch) {
        return ((HeadOfBranch)branch.getHeadOfBranch()).getMinimalWorkersForShift();
    }

    public static void getBranch(int branch) {
        Branch.getBranch(branch, null);
    }

    public static List<Branch> getRestOfBranches(Set<Integer> branches) {
        List<Branch> restOfBranches = new LinkedList<>();
        List<Integer> ids = Branch.getAllBranchesIDs();
        for (Integer id : ids) {
            if(!branches.contains(id)) {
                restOfBranches.add(Branch.getBranch(id, null));
            }
        }
        return restOfBranches;
    }
}
