#SuperMap iDesktop Cross8C ��չ����ָ��
##һ.����������
    SuperMap iDesktop Cross 8C��һ����ѭ GPL Э��Ŀ�Դ��Ʒ���ò�Ʒ����SuperMap iObjects Java 8C�����Ʒ������ֻ�����������Ʒ����ɼ��ɣ�����ա�SuperMap iObjects Java 8C tar ��ʹ��ָ�ϡ����������ɡ�
##��.���û�������
###2.1.����JAVA��������
��JAVA��·������ϵͳ�Ļ��������У�����Ѿ����ú�JDK���л��������������˲��� 

*    Windows
    
  a)	�Ҽ�����������ѡ�����ԡ���Ȼ�����ε�����߼�ϵͳ����-�߼�-����������

  b)	�ڡ�ϵͳ���������½�JAVA_HOME����������ֵ����ΪJAVA����Ŀ¼�����磺��C:\Program Files\Java\jdk1.7.0_67����

  c)	��ϵͳ�����е�Path��� :%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;
*	Linux

  a)	���ն�������sudo gedit /etc/profile���� profile �ļ���

  b)	���ļ�ĩβ���룺
 
         export JAVA_HOME=��`�˴��滻ΪJAVAĿ¼`��

         export PATH=$JAVA_HOME/bin:$PATH

  c)	����ϵͳ��������Ч��

###2.2 ����Eclipse��OSGi��������
Equinox ����� Eclipse ��֯���� OSGi Release 4 ��һ��ʵ�ֿ�ܣ���ʵ���� OSGi �淶�ĺ��Ŀ�ܺ�����׼��ܷ����ʵ�֡�

����[Eclipse����](http://www.eclipse.org/downloads/)����ָ���Ŀ����汾��Eclipse for RCP and RAP Developers���ð汾�Ѿ�������OSGi������������ֻ��Ҫ���½�������̣�Plug-in Project����ʱ��ѡ��OSGi Framework���ɡ�

###2.3 ����SuperMap iObjects Java 8C�����������
����Ļ�����������ֱ����ϵͳ�������������ã�Ҳ�����ڹ��������ã����ɸ����Լ����������ѡ��

####2.3.1 ����SuperMap iObjects Java 8C���

����[SuperMap �ٷ���վ](http://www.supermap.com/cn/)������[������Դ���Ĳ�Ʒ����ҳ��](http://support.supermap.com.cn/DownloadCenter/ProductPlatform.aspx )������Ŀ��ƽ̨�� SuperMap iObjects Java 8C �����Ʒ��

* ��ϵͳ���������������������
 
    �������·����ӵ����������У���������ú�������������������˲���

 *	Windows

    a)	��Path�м�������ĸ�Ŀ¼�����磺D:\Program Files\SuperMap\SuperMap iObiects Java 7C\Bin
    
 *	Linux

    a)	���ն�������sudo gedit /etc/profile���� profile �ļ���

    b)	���ļ�ĩβ����
       export LD_LIBRARY_PATH=��`���·��`��:$LD_LIBRARY_PATH

    c)	����ϵͳʹ������Ч
    
*  �ڹ��������������������

���빤��֮����iDesktopĿ¼�µ�iDesktop Frame Configuration.launch�ļ����һ���ѡ��Run As�е�Run Configurations���ڵ����������ѡ��OSGi Framework�е�iDesktop Frame Configuration,������Ҳര���е�Enviroment��ǩҳ��ѡ��New���� ��ť������ӻ�������������
�ڹ��������õķ���������iDesktop����ĵ��������Debug Configurations���������������Debug Configurations�����л���Environment��ǩҳ���½�һ��Environment variable���ڵ������������û���������
��ʹ�õ���Windows����ϵͳ����Name����Ӧ��дpath����ΪLinux����ϵͳ����дLD_LIBRARY_PATH����Value������дΪ���·�������OK ���档

###2.4���ù���֧�ֿ�
�ڸ�����������Ժ���Ҫ�����binĿ¼�е�����.jar�ļ��滻��/core/libĿ¼�£������jar��������
##3. ��д����

###3.1 ���빤��

����eclipse֮������ѡ�� File �C Import �C General �C Exitsting Project into Workspace �C Browse�������ļ�ѡ�񴰿ڡ��ҵ����������ļ��к���Finish��ɵ��롣
###3.2  �½���Ŀ
���ε��File �C New - Project����New Project���ڡ�ѡ��Plug-in DevelopmentĿ¼�µ�Plug-in Project�����Next���빤������, ����OSGI framework ѡ��Equinox ,���Next��Execution Environment����ΪJDK1.7�������Finish��ť��������½���
###3.3 ���������ϵ
��Ӷ�Core��Controls���������������

* ���½��Ĺ������Ҽ������Ҽ��˵�������ѡ�� Bulid  Path - Configure Bulid Path���ڵ��������ҷ�ѡ��Projects��Ȼ������Add������ť�����Core��Controls��Ŀ��Ȼ��ѡ��Libraries���Ƴ�Plug-in Dependencies�������OK�����ӡ�
*	�򿪹���META-INFĿ¼�µ�MANIFEST.MF�ļ�����Dependenciesҳ���е�Required Plug-insҳ���е����Add������ť���Controls��Core����

###3.4    ��д����
*	�����ɵ�Activator���Start()��������ӳ�ʼ������Ĵ��룺
```java
Application.getActiveApplication().getPluginManager().addPlugin(��SuperMap.Desktop.SampleCode��, bundleContext.getBundle());
```
�����ע�ᵽ��Ŀ�С�
����SuperMap.Desktop.SampleCode��Ϊ������ƣ�bundleContext.getBundle()ΪBundle����
*	Ȼ�󴴽������࣬�̳�CtrlAction�����е�run() ����Ϊִ��ʱ���õĺ�����enable() �������ع����Ƿ���á�
*	���������ļ�������ĿĿ¼��\WorkEnvironment\Default�����½������ļ����ɲο����߰��е�SampleCodeWorkEnvironment.xml�ļ�������ӡ�

###3.5 ���������ȼ������Թ���
�ڹ��������õķ���������iDesktop����ĵ��������Debug Configurations���������������Debug Configurations�����л���Bundles��ǩҳ���ҵ��¼ӵĲ��������StartLever����ֵԽ�󣬲������Խ��Ȼ����Run���г���