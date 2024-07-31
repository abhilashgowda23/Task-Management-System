package main.java;

import main.java.priority.PriorityStatus;
import main.java.task.Task;
import main.java.task.TaskManagerService;
import main.java.task.TaskStatus;
import main.java.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManagerDriver {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        atomicInteger.incrementAndGet();
        TaskManagerService taskManagerService = TaskManagerService.getInstance();
        User user1 = new User("USER1", "Abhilash", "abhilash@gmail.com");
        User user2 = new User("USER2", "Niharika", "niharika@gmail.com");
        User user3 = new User("USER3", "Manoj", "niharika@gmail.com");
        User user4 = new User("USER4", "Kiran", "niharika@gmail.com");

        Task task1 = new Task("JIRA" + atomicInteger.getAndIncrement(), "Dev", "Code changes", new Date(), PriorityStatus.HIGH_PRIORITY.ordinal(), user1);
        Task task2 = new Task("JIRA" + atomicInteger.getAndIncrement(), "QA", "Testing in E1", new Date(), PriorityStatus.MEDIUM_PRIORITY.ordinal(), user2);
        Task task3 = new Task("JIRA" + atomicInteger.getAndIncrement(), "Dev", "Junit test cases", new Date(), PriorityStatus.HIGH_PRIORITY.ordinal(), user1);
        Task task4 = new Task("JIRA" + atomicInteger.getAndIncrement(), "QA", "Testing in E2", new Date(), PriorityStatus.MEDIUM_PRIORITY.ordinal(), user3);
        Task task5 = new Task("JIRA" + atomicInteger.getAndIncrement(), "Devops", "Deployment in pre-prod", new Date(), PriorityStatus.LOW_PRIORITY.ordinal(), user4);
        //Task task4 = new Task("JIRA3" , "XXX", "Code changes", new Date(), 1, user1);



        // Add tasks to the task manager
        taskManagerService.createTask(task1);
        taskManagerService.createTask(task2);
        taskManagerService.createTask(task3);
        taskManagerService.createTask(task4);
        taskManagerService.createTask(task5);
        taskManagerService.addUser(user1);
        taskManagerService.addUser(user2);
        taskManagerService.addUser(user3);
        taskManagerService.addUser(user4);

        System.out.println("*****Tasks List*****");
        taskManagerService.getTasks();
        System.out.println("\n******Users List******");
//        taskManagerService.getUsers();
        taskManagerService.showUsers();

        // All tasks and User details
        System.out.println("\nUsers and Tasks Details Co-Relation\n");
        taskManagerService.showTasksAndUsers();

        //update task
        task2.setDescription("Testing in QA Server and E1");
        taskManagerService.updateTask(task2);
        System.out.println("\nUpdated details");
        System.out.println("User name =" + task2.getAssignedUser().getName() + ", Task Title=" + task2.getTitle() + ", Task Description=" + task2.getDescription() + " ]\n");

        //search task
        List<Task> searchedTasks = taskManagerService.searchTasks("Dev");
        System.out.println("=>>> Searched Tasks \n");
        for (Task task : searchedTasks) {
            System.out.println("User name =" + task.getAssignedUser().getName() + ", Task Title=" + task.getTitle() + ", Task Description=" + task.getDescription() + " ]");
        }




        //filter task
        List<Task> filteredTasks = taskManagerService.filterTasks(TaskStatus.PENDING, new Date(0), new Date(), PriorityStatus.HIGH_PRIORITY.ordinal());
        System.out.println("\n=>>> Filtered Tasks \n");
        for (Task task : filteredTasks) {
            System.out.println("User name =" + task.getAssignedUser().getName() + ", Task Title=" + task.getTitle() + ", Task Description=" + task.getDescription() + " ]");
        }

        //
        // mark task status completed
        taskManagerService.markTaskStatus(task1);

        //get the task history for user
        System.out.println("\n=>>>Get the task history for user");
        List<Task> historyTasks = taskManagerService.getUserTaskHistory(user1);
        for (Task historyTask : historyTasks) {
            System.out.println("User name =" + historyTask.getAssignedUser().getName() + ", Task Title=" + historyTask.getTitle() + ", Task Priority=" + historyTask.getPriority() + ", Task Description=" + historyTask.getDescription() + " ]");
        }


    }
}
