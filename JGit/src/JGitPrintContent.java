/** GIT sample code with BSD license. Copyright by Steve Jin */
 
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
 
 
public class JGitPrintContent
{
	
	public static void main(String[] args) throws IOException, GitAPIException {
		System.out.println("Git Print log");
	    Repository repo = new FileRepository("C:/git centerstone/trunk/trunk");
	    Git git = new Git(repo);
	    RevWalk walk = new RevWalk(repo);

	    List<Ref> branches = git.branchList().call();
	    
	    System.out.println("branch list size is  "+branches.size());

	    for (Ref branch : branches) {
	        String branchName = branch.getName();

	        System.out.println("Commits of branch: " + branch.getName());
	        System.out.println("-------------------------------------");

	        Iterable<RevCommit> commits = git.log().all().call();

	        for (RevCommit commit : commits) {
	            boolean foundInThisBranch = false;

	            RevCommit targetCommit = walk.parseCommit(repo.resolve(
	                    commit.getName()));
	            for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
	                if (e.getKey().startsWith(Constants.R_HEADS)) {
	                    if (walk.isMergedInto(targetCommit, walk.parseCommit(
	                            e.getValue().getObjectId()))) {
	                        String foundInBranch = e.getValue().getName();
	                        if (branchName.equals(foundInBranch)) {
	                            foundInThisBranch = true;
	                            break;
	                        }
	                    }
	                }
	            }

	            if (foundInThisBranch) {
	                System.out.println(commit.getName());
	                System.out.println(commit.getAuthorIdent().getName());
	                System.out.println(new Date(commit.getCommitTime()));
	                System.out.println(commit.getFullMessage());
	            }
	        }
	    }
	}	
	
  /*public static void main(String[] args) throws Exception
  {
    File gitWorkDir = new File("C:/git centerstone/trunk/trunk");
    Git git = Git.open(gitWorkDir);
    Repository repo = git.getRepository();
 
    ObjectId lastCommitId = repo.resolve(Constants.HEAD);
 
    RevWalk revWalk = new RevWalk(repo);
    RevCommit commit = revWalk.parseCommit(lastCommitId);
 
    RevTree tree = commit.getTree();
 
    TreeWalk treeWalk = new TreeWalk(repo);
    treeWalk.addTree(tree);
    treeWalk.setRecursive(true);
    treeWalk.setFilter(PathFilter.create("file1.txt"));
    if (!treeWalk.next()) 
    {
      System.out.println("Nothing found!");
      return;
    }
 
    ObjectId objectId = treeWalk.getObjectId(0);
    ObjectLoader loader = repo.open(objectId);
 
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    loader.copyTo(out);
    System.out.println("file1.txt:\n" + out.toString());
    }*/
}