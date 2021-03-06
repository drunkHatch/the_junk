package com.example.mayingnan.project301;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mayingnan.project301.controller.TaskController;
import com.example.mayingnan.project301.controller.UserListController;

import org.junit.Test;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by julianstys on 2018-02-25.
 */

public class TaskTest {

    /*
    @Test
    public void testTaskContructor(){
        ArrayList<Bid> bidList = new ArrayList<Bid>();
        Photo emptyPhoto = new Photo();

        Task task = new Task("Fetch","Fetchcar","Michael",null,"bidding","random address",bidList,emptyPhoto);

        assertEquals("Michael", task.getTaskRequester());
        assertEquals("Michael", task.getTaskRequester());

        assertNull(task.getTaskProvider());
        task.setTaskProvider("James");
        assertEquals("James", task.getTaskProvider());


    }
    @Test
    public void testTaskNoConstructor(){
        ArrayList<Bid> bidList = new ArrayList<Bid>();
        Photo emptyPhoto = new Photo();
        Task task = new Task("Fetch car","Fetch my car","Michael",null,"bidding","random address",bidList,emptyPhoto);
        task.setTaskName("Fetch car");
        task.setTaskDetails("Fetch my car from lister hall");
        task.setTaskAddress("HUB mall");
        task.setTaskRequester("Julian");
        task.setTaskProvider(null);
        assertEquals("Julian", task.getTaskRequester());
        task.setTaskRequester("Chris");
        assertEquals("Chris", task.getTaskRequester());


    }
    @Test
    */
    // TODO more failing test cases
    // add passed w8 for more cases
    @Test
    public void testAddTask(){

        TaskController.addTask addTaskCtl = new TaskController.addTask();
        Task task = new Task();
        task.setTaskName("hi");

        addTaskCtl.execute(task);

        AsyncTask.Status taskStatus;
        do {
            taskStatus = addTaskCtl.getStatus();
        } while (taskStatus != AsyncTask.Status.FINISHED);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!(task.getId() == null)){
            if (!task.getId().isEmpty()){
                assertTrue(true);
            }
        }
    }

    @Test
    // basic passed, w8 for failure test cases
    public void testGetTask(){
        TaskController.addTask addTaskCtl = new TaskController.addTask();
        Task task = new Task();
        Task rt_task = new Task();
        ArrayList<Task> single_task = new ArrayList<Task>();


        task.setTaskName("gg");

        addTaskCtl.execute(task);

        AsyncTask.Status taskStatus;
        do {
            taskStatus = addTaskCtl.getStatus();
        } while (taskStatus != AsyncTask.Status.FINISHED);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TaskController.getTaskById getTask = new TaskController.getTaskById();
        if (task.getId() == null){
            Log.i("Failure add", task.getId());
            assertTrue(false);
        }
        getTask.execute(task.getId());

        try {
            rt_task = getTask.get();
            Log.i("Success", "message");

            if (task.getTaskName().equals(rt_task.getTaskName())){
                assertTrue(true);
            }
            else{
                assertTrue(false);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("Error", "return fail");

        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.i("Error", "not getting anything");
        }

    }

    @Test
    public void testDeleteTask(){
        // add a sample test to db
        TaskController.addTask addTaskCtl = new TaskController.addTask();
        Task task = new Task();
        task.setTaskName("hi");

        addTaskCtl.execute(task);

        AsyncTask.Status taskStatus;
        do {
            taskStatus = addTaskCtl.getStatus();
        } while (taskStatus != AsyncTask.Status.FINISHED);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // now delete it
        TaskController.deleteTaskById deleteTask = new TaskController.deleteTaskById(task.getId());
        TaskController.getTaskById getTask = new TaskController.getTaskById();

        deleteTask.execute(task.getId());

        AsyncTask.Status taskStatus2;
        do {
            taskStatus2 = deleteTask.getStatus();
        } while (taskStatus2 != AsyncTask.Status.FINISHED);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getTask.execute(task.getId());

        try {
            Task rt_task = getTask.get();
            Log.i("Success", "message");

            if (rt_task == null){
                assertTrue(true);
            }else {
                Log.i("Error", "holyshit it got something: "+ rt_task.getTaskName());
                assertTrue(false);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("Error", "return fail");

        } catch (ExecutionException e) {
            e.printStackTrace();
            assertTrue(true);
            Log.i("Error", "not getting anything");
        }
    }

    @Test
    public void requesterUpdateTaskTest(){
        TaskController.requesterUpdateTask updateTask = new TaskController.requesterUpdateTask();
        TaskController.addTask addTask = new TaskController.addTask();
        TaskController.getTaskById getTask = new TaskController.getTaskById();
        Task test_task = new Task();
        Task empty_task = new Task();

        // init test task info, all info should be tested
        test_task.setTaskDetails("Details");
        test_task.setTaskName("Test");
        test_task.setTaskProvider("A donkey");
        test_task.setTaskRequester("A snake");

        // now add test task to db
        addTask.execute(test_task);

        // w8 5sec for update to be done
        AsyncTask.Status taskStatus;
        do {
            taskStatus = addTask.getStatus();
        } while (taskStatus != AsyncTask.Status.FINISHED);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // now update local test task
        test_task.setTaskDetails("Details-1");
        test_task.setTaskName("Test-1");
        test_task.setTaskProvider("donkey-1");
        test_task.setTaskRequester("snake-1");

        // now update the local to db
        updateTask.execute(test_task);

        AsyncTask.Status taskStatus2;
        do {
            taskStatus2 = addTask.getStatus();
        } while (taskStatus2 != AsyncTask.Status.FINISHED);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // now try to get cloud task info
        getTask.execute(test_task.getId());
        try {
            empty_task = getTask.get();

            if (    empty_task.getTaskName().equals(test_task.getTaskName())    &&
                    empty_task.getTaskDetails().equals(test_task.getTaskDetails()) &&
                    empty_task.getTaskProvider().equals(test_task.getTaskProvider()) &&
                    empty_task.getTaskRequester().equals(test_task.getTaskRequester())
               )
            {
                assertTrue(true);
            }
            else{
                Log.i("Error: WTF", empty_task.getTaskName() + empty_task.getTaskDetails() + empty_task.getTaskProvider() + empty_task.getTaskRequester());
                assertTrue(false);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            assertTrue(false);
            Log.i("Error", "return fail");

        } catch (ExecutionException e) {
            e.printStackTrace();
            assertTrue(false);
            Log.i("Error", "not getting anything");
        }

    }

    /*
    @Test
    public void searchTaskByKeywordTest(){
        TaskController tc = new TaskController();
        Task task = new Task();
        task.setTaskName("hihi");
        tc.addTask(task);
        assertTrue(tc.searchTaskByKeyword("hi").contains(task));

    }
    @Test
    public void searchBiddenTasksOfThisProviderTest(){
        String userName = "me";
        TaskController tc = new TaskController();
        Task task = new Task();
        task.setTaskProvider(userName);
        task.setTaskName("hihi");
        task.setTaskStatus("bidding");
        tc.addTask(task);
        assertTrue(tc.searchBiddenTasksOfThisProvider(userName).contains(task));

    }
    @Test
    public void searchAssignTasksOfThisProviderTest(){
        String userName = "me";
        TaskController tc = new TaskController();
        Task task = new Task();
        task.setTaskProvider(userName);
        task.setTaskName("hihi");
        task.setTaskStatus("processing");
        tc.addTask(task);
        assertTrue(tc.searchAssignTasksOfThisProvider(userName).contains(task));



    }
    @Test
    public void searchAllTasksOfThisRequesterTest(){
        String userName = "me";
        TaskController tc = new TaskController();
        Task task = new Task();
        task.setTaskProvider(userName);
        task.setTaskName("hihi");
        task.setTaskStatus("processing");

        Task task2= new Task();
        task2.setTaskProvider(userName);
        task2.setTaskName("hihihi2");
        task2.setTaskStatus("finished");
        tc.addTask(task);
        assertTrue(tc.searchAllTasksOfThisRequester(userName).contains(task));
        assertTrue(tc.searchAllTasksOfThisRequester(userName).contains(task2));



    }
    @Test
    public void searchAllRequestingTasksTest(){
        TaskController tc= new TaskController();
        Task task = new Task();
        task.setTaskStatus("finished");

        Task task2 = new Task();
        task.setTaskStatus("requesting");

        assertEquals(tc.searchAllRequestingTasks().get(0),task2);

        assertEquals(tc.searchAllRequestingTasks().size(),1);



    }
    @Test
    public void searchTaskByTaskNameTest(){
        TaskController tc = new TaskController();
        Task task = new Task();
        task.setTaskName("hi");
        assertEquals(tc.searchTaskByTaskName("hi"),task);



    }
    */

}
