package com.mjc.entity;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 服务器相关信息
 */
@Data
public class Server {

    private static final int OSHI_WAIT_SECOND = 1000;

    /** CPU相关信息 */
    private Cpu cpu = new Cpu();
    /** 內存相关信息 */
    private Mem mem = new Mem();
    /** JVM相关信息 */
    private Jvm jvm = new Jvm();
    /** 服务器相关信息 */
    private Sys sys = new Sys();
    /** 磁盘相关信息 */
    private List<SysFile> sysFiles = new ArrayList<>();

    public void copyTo() throws Exception {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();

        setCpuInfo(hal.getProcessor());
        setMemInfo(hal.getMemory());
        setSysInfo();
        setJvmInfo();
        setSysFiles(si.getOperatingSystem());
    }

    /** 设置CPU信息 */
    private void setCpuInfo(CentralProcessor processor) {
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            Thread.sleep(OSHI_WAIT_SECOND);
        } catch (InterruptedException ignored) {}
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softIrq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softIrq + steal;
        
        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setTotal(totalCpu);
        cpu.setSys(cSys);
        cpu.setUsed(user);
        cpu.setWait(iowait);
        cpu.setFree(idle);
    }

    /** 设置内存信息 */
    private void setMemInfo(GlobalMemory memory) {
        mem.setTotal(memory.getTotal());
        mem.setUsed(memory.getTotal() - memory.getAvailable());
        mem.setFree(memory.getAvailable());
    }

    /** 设置服务器信息 */
    private void setSysInfo() {
        Properties props = System.getProperties();
        sys.setComputerName(getHostName());
        sys.setComputerIp(getHostIp());
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
    }

    /** 设置Java虚拟机 */
    private void setJvmInfo() {
        Properties props = System.getProperties();
        jvm.setTotal(Runtime.getRuntime().totalMemory());
        jvm.setMax(Runtime.getRuntime().maxMemory());
        jvm.setFree(Runtime.getRuntime().freeMemory());
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setHome(props.getProperty("java.home"));
        jvm.setStartTime(DateUtil.formatDateTime(new Date(ManagementFactory.getRuntimeMXBean().getStartTime())));
        // 计算运行时间
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        Date date = new Date(time);
        jvm.setRunTime(DateUtil.formatBetween(date, new Date()));
    }

    /** 设置磁盘信息 */
    private void setSysFiles(OperatingSystem os) {
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setTypeName(fs.getType());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            sysFile.setUsage(NumberUtil.mul(NumberUtil.div(used, total, 4), 100));
            sysFiles.add(sysFile);
        }
    }

    /** 字节转换 */
    public String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) return String.format("%.1f GB", (float) size / gb);
        if (size >= mb) return String.format("%.1f MB", (float) size / mb);
        if (size >= kb) return String.format("%.1f KB", (float) size / kb);
        return String.format("%d B", size);
    }
    
    private String getHostIp(){ try{ return java.net.InetAddress.getLocalHost().getHostAddress(); }catch(Exception e){ return "127.0.0.1";} }
    private String getHostName(){ try{ return java.net.InetAddress.getLocalHost().getHostName(); }catch(Exception e){ return "未知";} }

    // --- 内部类定义 ---

    @Data
    public static class Cpu {
        private int cpuNum;
        private double total;
        private double sys;
        private double used;
        private double wait;
        private double free;
        public double getSys() { return NumberUtil.mul(NumberUtil.div(sys, total, 4), 100); }
        public double getUsed() { return NumberUtil.mul(NumberUtil.div(used, total, 4), 100); }
        public double getWait() { return NumberUtil.mul(NumberUtil.div(wait, total, 4), 100); }
        public double getFree() { return NumberUtil.mul(NumberUtil.div(free, total, 4), 100); }
    }

    @Data
    public static class Mem {
        private double total;
        private double used;
        private double free;
        public double getTotal() { return NumberUtil.div(total, (1024 * 1024 * 1024), 2); } // GB
        public double getUsed() { return NumberUtil.div(used, (1024 * 1024 * 1024), 2); } // GB
        public double getFree() { return NumberUtil.div(free, (1024 * 1024 * 1024), 2); } // GB
        public double getUsage() { return NumberUtil.mul(NumberUtil.div(used, total, 4), 100); }
    }

    @Data
    public static class Jvm {
        private double total;
        private double max;
        private double free;
        private String version;
        private String home;
        private String startTime;
        private String runTime;
        public double getTotal() { return NumberUtil.div(total, (1024 * 1024), 2); } // MB
        public double getMax() { return NumberUtil.div(max, (1024 * 1024), 2); } // MB
        public double getFree() { return NumberUtil.div(free, (1024 * 1024), 2); } // MB
        public double getUsed() { return NumberUtil.div(total - free, (1024 * 1024), 2); } // MB
        public double getUsage() { return NumberUtil.mul(NumberUtil.div(total - free, total, 4), 100); }
    }

    @Data
    public static class Sys {
        private String computerName;
        private String computerIp;
        private String userDir;
        private String osName;
        private String osArch;
    }

    @Data
    public static class SysFile {
        private String dirName;
        private String typeName;
        private String total;
        private String free;
        private String used;
        private double usage;
    }
}