package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.processing.MapCacheBuilder;
import com.supermap.mapping.Map;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiProcessCacheBuilder {

    /**
     * ���ö���̽��в���. TODO: �ܹ�ʹ�ö������ͼ
     *
     * @param args
     * @throws IOException
     * @throws FileNotFoundException
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.out.println("please input args: workspaceFile mapName taskList cacheRoot processCount[options] mergeCount[options]");
            return;
        }

        String workspaceFile = args[0];
        String mapName = args[1];
        String taskListFile = args[2];
        String cacheRoot = args[3];

        // Ĭ��Ϊ3������
        int processCount = 3;
        if (args.length > 4) {
            processCount = Integer.valueOf(args[4]);
        }

        int mergeCount = 1;
        if (args.length > 5) {
            mergeCount = Integer.valueOf(args[5]);
        }

        File dir = new File(new File(taskListFile).getParent() + "/done");
        if (!dir.exists()) {
            dir.mkdir();
        }

        if (taskListFile.toLowerCase().endsWith(".sci")) {
            // �ӽ���������һ����Ϊ�ӽ���ʱtaskListFile Ϊsci�ļ��������������嵥�ļ�
            Run(workspaceFile, mapName, taskListFile, cacheRoot);
        } else {
            // ��ȡ�ļ������ǵ��ļ��Ƚϴ󣬶���һ�У���ִ��һ��
            // Ĭ�ϲ�д��־�ļ�������̵�ʱ����д�ļ�
            LogWriter.setWriteToFile(true);
            // д��������Ϣ���Ա����鿴��־���з���
            LogWriter log = LogWriter.getInstance();

            InetAddress ia = InetAddress.getLocalHost();
            String localname = ia.getHostName();
            String localip = ia.getHostAddress();


            log.writelog(String.format("Run Workspace:%s Map:%s Tasks:%s CacheRoot%s processCount:%d mergeCount:%d, at computer:%s, ip:%s", workspaceFile, mapName, taskListFile, cacheRoot, processCount, mergeCount, localname, localip));

            File tasks = new File(taskListFile);
            BufferedReader taskReader = new BufferedReader(new InputStreamReader(new FileInputStream(tasks), "UTF-8"));
            String sciFile = null;
            String folder = tasks.getParent();

            // �����ڴ��У������ظ�����
            ArrayList<String> allLines = new ArrayList<String>();
            String line;
            while ((line = taskReader.readLine()) != null) {
                allLines.add(line);
            }
            taskReader.close();

            // ������С��2ʱ��ʹ�õ�ǰ������ͼ
            if (processCount < 2) {
                RunAllCurrentProc(allLines, folder, workspaceFile, mapName, cacheRoot);
            } else {
                RunAllMultiProc(allLines, folder, workspaceFile, mapName, cacheRoot, processCount, mergeCount);
            }
        }
    }

    public static void RunBySubProc(List<String> allTasks, String sciFolder, String workspaceFile, String mapName, String cacheRoot, int processCount, int mergeCount) throws Exception {
        LogWriter log = LogWriter.getInstance();
        //java options
        RuntimeMXBean runtimeMBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmArgs = runtimeMBean.getInputArguments();
        // �����ӽ��̣���������������������������Ϊoutput���ˣ������������
        List<SubprocessThread> threaList = Collections.synchronizedList(new ArrayList<SubprocessThread>());
        int merged = 0;
        StringBuffer buffer = new StringBuffer();
        int taskSize = allTasks.size();
        int lastIndex = taskSize - 1;
        for (int i = 0; i < taskSize; i++) {
            String sciFile = allTasks.get(i);
            sciFile = sciFolder + "/" + sciFile;
            File sci = new File(sciFile);

            if (!sci.exists()) {
                LogWriter.getInstance().writelog(String.format("sciFile: %s does not exist. Maybe has done at before running. ", sciFile));
                continue;
            }
            buffer.append(";");
            buffer.append(sciFile);
            merged++;
            // ���Ϊ���һ��һ�������ˣ�����mereCountҲ��Ҫִ�У�����continue�����������
            if (merged < mergeCount && i != lastIndex) {
                continue;
            }

            // �ϲ��������ˣ���ʼ�����ӽ���
            ArrayList<String> arguments = new ArrayList<String>();
            arguments.add("java");
            arguments.addAll(jvmArgs);
            arguments.add("-cp");
            arguments.add(System.getProperty("java.class.path"));
            arguments.add(MultiProcessCacheBuilder.class.getName());
            arguments.add(workspaceFile);
            arguments.add(mapName);
            arguments.add(buffer.substring(1));
            arguments.add(cacheRoot);

            buffer = new StringBuffer();
            merged = 0;

            SubprocessThread thread = new SubprocessThread(arguments);
            threaList.add(thread);
            thread.start();

            // �ȴ�һ����������������µ�����
            while (threaList.size() >= processCount) {
                //�ж�time out���Ա�kill��������Ľ���
                int tSize = threaList.size();
                for (int index = tSize - 1; index > -1; index--) {
                    SubprocessThread t = threaList.get(index);
                    t.timeout();
                    if (t.isExit) {
                        threaList.remove(index);
                    }
                }
                Thread.sleep(1000);
                log.flush();
            }
        }
        log.flush();


        // �ȴ�������
        while (threaList.size() > 0) {
            //�ж�time out���Ա�kill��������Ľ���
            int tSize = threaList.size();
            for (int index = tSize - 1; index > -1; index--) {
                SubprocessThread t = threaList.get(index);
                t.timeout();
                if (t.isExit) {
                    threaList.remove(index);
                }
            }
            Thread.sleep(1000);
            log.flush();
        }
        log.flush();
    }

    public static void RunAllMultiProc(List<String> allTasks, String sciFolder, String workspaceFile, String mapName, String cacheRoot, int processCount, int mergeCount) throws Exception {

        // �����ӽ��̣����ǽ���������ͬʱ���ڽ��̸���
        LogWriter log = LogWriter.getInstance();
        long start = System.currentTimeMillis();

        // ��������ʼ����
        RunBySubProc(allTasks, sciFolder, workspaceFile, mapName, cacheRoot, processCount, mergeCount);

        // ����ִ�д�ǰʧ�ܵ��������Ժ���ʧ�ܣ������ͨ����־�ķ�ʽ���
        ArrayList<String> tryAgainTasks = new ArrayList<String>();
        for (String task : allTasks) {
            String sciFile = sciFolder + "/" + task;
            File sci = new File(sciFile);
            if (!sci.exists()) {
                continue;
            }
            tryAgainTasks.add(task);
            LogWriter.getInstance().writelog(String.format("the failure sciFile: %s try again", sciFile));
        }

        // ���̹߳����ʱ����������UDB������Ĭ�����ٺϲ�4������
        int newMergeCount = tryAgainTasks.size() / processCount;
        if (newMergeCount < 4) {
            newMergeCount = 4;
        }
        // �����һ��ʧ�ܵ�����,���ﲻ�ϲ�����
        RunBySubProc(tryAgainTasks, sciFolder, workspaceFile, mapName, cacheRoot, processCount, newMergeCount);

        // �ڶ���ִ��ʧ�ܣ�ͨ����־ͨ����ȥ
        for (String task : tryAgainTasks) {
            String sciFile = sciFolder + "/" + task;
            File sci = new File(sciFile);
            if (!sci.exists()) {
                continue;
            }
            LogWriter.getInstance().writelog(String.format("sciFile: %s fail.", sciFile));
        }

        long cost = System.currentTimeMillis() - start;
        String lastLog = String.format("sci File count:%d, cost(ms):%d", allTasks.size(), cost);
        log.writelog(lastLog);
        log.flush();
        log.closs();
        System.out.println(lastLog);
    }

    public static void RunAllCurrentProc(List<String> allTasks, String sciFolder, String workspaceFile, String mapName, String cacheRoot) {

        for (String sciFile : allTasks) {
            sciFile = sciFolder + "/" + sciFile;
            File sci = new File(sciFile);
            if (!sci.exists()) {
                LogWriter.getInstance().writelog(String.format("sciFile: %s does not exist. Maybe has done at before running. ", sciFile));
                continue;
            }
            Run(workspaceFile, mapName, sciFile, cacheRoot);
        }

        // ����ִ�д�ǰʧ�ܵ��������Ժ���ʧ�ܣ������ͨ����־�ķ�ʽ���
        ArrayList<String> tryAgainTasks = new ArrayList<String>();
        for (String task : allTasks) {
            String sciFile = sciFolder + "/" + task;
            File sci = new File(sciFile);
            if (!sci.exists()) {
                continue;
            }
            tryAgainTasks.add(task);
            LogWriter.getInstance().writelog(String.format("the failure sciFile: %s try again", sciFile));
            Run(workspaceFile, mapName, sciFile, cacheRoot);
        }

        // �������ʧ�ܵģ���ͨ����־д��
        for (String task : tryAgainTasks) {
            String sciFile = sciFolder + "/" + task;
            File sci = new File(sciFile);
            if (!sci.exists()) {
                continue;
            }
            LogWriter.getInstance().writelog(String.format("sciFile: %s fail.", sciFile));
        }
    }

    public static void Run(String workspaceFile, String mapName, String sciFiles, String targetRoot) {

        long start = System.currentTimeMillis();
        LogWriter log = LogWriter.getInstance();
        String pid = LogWriter.getPID();

        // sciFile ֧�ֶ���ļ�
        String[] sciFileArray = sciFiles.split(";");
        Workspace wk = new Workspace();
        WorkspaceConnectionInfo info = new WorkspaceConnectionInfo(workspaceFile);
        wk.open(info);
        Map map = new Map(wk);
        map.open(mapName);
        log.writelog(String.format("start sciCount:%d , PID:%s", sciFileArray.length, pid));
        log.writelog(String.format("init PID:%s, cost(ms):%d", LogWriter.getPID(), System.currentTimeMillis() - start));

        for (String sciFile : sciFileArray) {
            long oneStart = System.currentTimeMillis();
            File sci = new File(sciFile);
            if (!sci.exists()) {
                LogWriter.getInstance().writelog(String.format("sciFile: %s does not exist. Maybe has done at before running. ", sciFile));
                continue;
            }
            // ��ӿհ��ǵĶ��룬���׺���ͨ��excel������־����
            MapCacheBuilder builder = new MapCacheBuilder();
            // ��sci�ź��棬����ʹ��sci��HashCode
            builder.setMap(map);
            builder.fromConfigFile(sciFile);
            builder.setOutputFolder(targetRoot);
            // ����¼resumable��Ϣ
            builder.resumable(false);

            boolean result = builder.buildWithoutConfigFile();
            // ��sci�ļ��Ƶ�doneĿ¼��
            String doneDir = (sci.getParent() + "/done");

            if (result) {
                sci.renameTo(new File(doneDir, sci.getName()));
            }
            builder.dispose();
            long end = System.currentTimeMillis();
            log.writelog(String.format("%s %s done,PID:%s, cost(ms):%d, done", sciFile, String.valueOf(result), LogWriter.getPID(), end - oneStart));
            log.flush();
        }

        // TODO: �Ƿ���Ҫ���Ǵ�ӡ�������̵�ƽ����ʱ

        map.close();
        map.dispose();
        wk.dispose();

    }

}
