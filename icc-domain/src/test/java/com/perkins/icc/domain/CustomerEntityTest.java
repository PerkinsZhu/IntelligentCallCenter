package com.perkins.icc.domain;


import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.HashSet;
import java.util.Set;

public class CustomerEntityTest {

    public void testCustomerConflict() {
        System.out.println("Please mock gatewayimpl, test pure Domain Knowledge");
    }


    @Test
    public void testHelloWorld() {
        // KieServices就是一个中心，通过它来获取的各种对象来完成规则构建、管理和执行等操作。
        KieServices kieServices = KieServices.Factory.get();
        // 个知识仓库(包含了若干的规则、流程、方法)的容器。
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        // 与drools引擎打交道的会话。
        KieSession kieSession = kieContainer.newKieSession("helloworld");
        // 执行所有规则，返回满足规则的数量
        int i = kieSession.fireAllRules();
        System.out.println(i);
        // 销毁对象
        kieSession.dispose();
    }

    @Test
    public void testHelloWorld2() {
        // Class.forName( "org.drools.compiler.kie.builder.impl.KieServicesImpl" ).newInstance()
        KieServices kieServices = KieServices.Factory.get();
        //会去 resource/META-INF 下找 kmodule.xml 文件
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        // kmodule.xml中<ksession name="helloworld"/> name值
        KieSession kieSession = kieContainer.newKieSession("helloworld");
        //启用规则
        Set set=new HashSet();
        set.add("helloworld1");
        set.add("helloworld2");
        int a = kieSession.fireAllRules();
        /*FactHandle factHandle2 = kieSession.insert("456");
        int b = kieSession.fireAllRules(new RuleNameEndsWithAgendaFilter("helloWorld1"));
        kieSession.delete(factHandle2);*/
        kieSession.dispose();
    }
}
