package main.java.task;

import main.java.user.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TaskManagerService {


    static TaskManagerService taskManagerService;
    private final Map<String, Task> tasks;
    private final Map<String, List<Task>> userTasks;
    private final List<User> userList;


    private TaskManagerService() {
        tasks = new ConcurrentHashMap<>();
        userTasks = new ConcurrentHashMap<>();
        userList = new ArrayList<>();
    }

    public void getTasks() {
        tasks.values().stream().sorted(Comparator.comparing(Task::getId)).forEach(task ->
                System.out.println("ID=" + task.getId() + ", Title=" + task.getTitle() + ", Description=" + task.getDescription() + ", Status=" + task.getStatus()));

    }

    public void getUsers() {
        //         List<Task> searchedTasks = new ArrayList<>();
        //        for (Task task : tasks.values()) {
        //            if (task.getTitle().contains(message) || task.getDescription().contains(message)) {
        //                searchedTasks.add(task);
        //            }
        //        }
        //        return searchedTasks;
        // List<Task> flattasks = userTasks.values().stream().flatMap(List::stream).collect(Collectors.toList());
//        flattasks.stream().sorted().forEach(m ->
//                System.out.println(m.getAssignedUser().getName() + " " + m.getAssignedUser().getEmail()));
//        userTasks.values().stream().flatMap(List::stream).map(Task::getAssignedUser).sorted(Comparator.comparing(User::getId)).forEach(user ->
//                System.out.println(user.getId() + " " + user.getName() + " " + user.getEmail()));
    }


    public static synchronized TaskManagerService getInstance() {
        if (taskManagerService == null) {
            taskManagerService = new TaskManagerService();
        }
        return taskManagerService;
    }

    public void createTask(Task task) {
        tasks.put(task.getId(), task);
        assignTaskToUser(task.getAssignedUser(), task);

    }

    public void addUser(User user) {
        userList.add(user);

    }

    public void assignTaskToUser(User user, Task task) {
        userTasks.computeIfAbsent(user.getId(), k -> new ArrayList<>()).add(task);


    }

    public void updateTask(Task updatedTask) {
        Task existingTask = tasks.get(updatedTask.getId());
        if (Objects.nonNull(existingTask)) {
            synchronized (existingTask) {
                existingTask.setTitle(updatedTask.getTitle());
                existingTask.setDescription(updatedTask.getDescription());
                existingTask.setDueDate(updatedTask.getDueDate());
                existingTask.setPriority(updatedTask.getPriority());
                existingTask.setStatus(updatedTask.getStatus());
                User previousUser = existingTask.getAssignedUser();
                User newUser = updatedTask.getAssignedUser();
                if (!previousUser.equals(newUser)) {
                    unAssignTaskUser(previousUser, existingTask);
                    assignTaskToUser(newUser, existingTask);
                }
            }
        }


    }

    private void unAssignTaskUser(User previousUser, Task existingTask) {
        List<Task> tasks = userTasks.get(existingTask.getId());
        if (tasks != null) {
            tasks.remove(existingTask);
        }
    }

    public List<Task> searchTasks(String message) {

        List<Task> searchedTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.getTitle().contains(message) || task.getDescription().contains(message)) {
                searchedTasks.add(task);
            }
        }
        return searchedTasks;

        //2nd approach
        //  return  tasks.values().stream().filter(task->task.getTitle().contains(message)).collect(Collectors.toList());

    }

    public List<Task> filterTasks(TaskStatus status, Date startDate, Date endDate, int priority) {

        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.getStatus().equals(status) && task.getDueDate().compareTo(startDate) >= 0 && task.getDueDate().compareTo(endDate) <= 0 && task.getPriority() == priority) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
//   2nd approach
//        return tasks.values().stream().filter(task -> task.getPriority() == priority &&
//                task.getStatus().equals(status) &&
//                task.getDueDate().compareTo(startDate) >= 0 &&
//                task.getDueDate().compareTo(endDate) >= 0).collect(Collectors.toList());

    }

    public void markTaskStatus(Task task) {
        Task task1 = tasks.get(task.getId());
        task1.setStatus(TaskStatus.COMPLETED);

    }


    public List<Task> getUserTaskHistory(User user1) {
        return new ArrayList<>(userTasks.getOrDefault(user1.getId(), new ArrayList<>()));
    }

    public void showUsers() {
        userList.stream().sorted(Comparator.comparing(User::getId)).forEach(s -> System.out.println("ID=" + s.getId() + ", Name=" + s.getName() + ", Email=" + s.getEmail()));
    }

    public void showTasksAndUsers() {

        userTasks.values().stream().flatMap(List::stream).forEach(
                task ->
                        System.out.println("Name=" + task.getAssignedUser().getName() + ", Email=" + task.getAssignedUser().getEmail() + ", [ Task Title=" + task.getTitle() + ", Task Description=" + task.getDescription() + " ]"));

    }

    public void countTasksPerUser(String id) {

        long n = userTasks.values().stream().flatMap(List::stream).filter(task -> task.getAssignedUser().getId().equalsIgnoreCase(id)).count();
         userList.stream().filter(user -> user.getId().equalsIgnoreCase(id)).forEach( l->System.out.println("Total Tasks Assigned for " + l.getName()+" " + " is/are : " + n));


    }


    public void countTasksPerUserMethod() {
        for (Map.Entry<String, List<Task>> map : userTasks.entrySet()) {
            System.out.println(map.getKey() + " : " + map.getValue().size());
        }
    }
}




