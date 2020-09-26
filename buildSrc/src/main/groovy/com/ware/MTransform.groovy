package com.ware

import com.android.build.api.transform.*
import com.android.build.gradle.internal.scope.GlobalScope
import com.android.build.gradle.internal.scope.VariantScope
import com.android.build.gradle.internal.variant.BaseVariantData
import com.google.common.base.Joiner
import org.gradle.api.Project

import static com.android.builder.model.AndroidProject.FD_OUTPUTS

class MTransform extends Transform {

    static void inject(Project project, /*MatrixTraceExtension extension,*/ VariantScope variantScope) {
//
        GlobalScope globalScope = variantScope.getGlobalScope()
        BaseVariantData variant = variantScope.getVariantData()
        String mappingOut = Joiner.on(File.separatorChar).join(
                String.valueOf(globalScope.getBuildDir()),
                FD_OUTPUTS,
                "mapping", //InternalArtifactType.APK_MAPPING.INSTANCE.folderName   variant.getMappingFileProvider().get().files
                variant.name.capitalize())

        println("mapping file = $mappingOut;")
//
//        String traceClassOut = Joiner.on(File.separatorChar).join(
//                String.valueOf(globalScope.getBuildDir()),
//                FD_OUTPUTS,
//                "traceClassOut",
//                variantScope.getVariantConfiguration().getDirName());
//        Configuration config = new Configuration.Builder()
//                .setPackageName(variant.getApplicationId())
//                .setBaseMethodMap(extension.getBaseMethodMapFile())
//                .setBlackListFile(extension.getBlackListFile())
//                .setMethodMapFilePath(mappingOut + "/methodMapping.txt")
//                .setIgnoreMethodMapFilePath(mappingOut + "/ignoreMethodMapping.txt")
//                .setMappingPath(mappingOut)
//                .setTraceClassOut(traceClassOut)
//                .build();
//
//        try {
//            String[] hardTask = getTransformTaskName(extension.getCustomDexTransformName(), variant.getName());
//            for (Task task : project.getTasks()) {
//                for (String str : hardTask) {
//                    if (task.getName().equalsIgnoreCase(str) && task instanceof TransformTask) {
//                        TransformTask transformTask = (TransformTask) task;
//                        Log.i(TAG, "successfully inject task:" + transformTask.getName());
//                        Field field = TransformTask.class.getDeclaredField("transform");
//                        field.setAccessible(true);
//                        field.set(task, new MatrixTraceTransform(config, transformTask.getTransform()));
//                        break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }

    }

    private Project mProject

    MTransform(Project project) {
        mProject = project
    }

    /**
     * @return 运行时的名字 transformClassesWith + getName() + For + Debug或Release
     */
    @Override
    String getName() {
        return "MPlu"
    }

    /**
     * @return 需要处理的数据类型:CLASSES和RESOURCES
     * CLASSES代表处理的java的class文件，RESOURCES代表要处理java的资源
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

//    指Transform要操作内容的范围，官方文档Scope有7种类型：
//
//    EXTERNAL_LIBRARIES        只有外部库
//    PROJECT                       只有项目内容
//    PROJECT_LOCAL_DEPS            只有项目的本地依赖(本地jar)
//    PROVIDED_ONLY                 只提供本地或远程依赖项
//    SUB_PROJECTS              只有子项目。
//    SUB_PROJECTS_LOCAL_DEPS   只有子项目的本地依赖项(本地jar)。
//    TESTED_CODE                   由当前变量(包括依赖项)测试的代码
    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    //指明当前Transform是否支持增量编译
    @Override
    public boolean isIncremental() {
        return false;
    }

//    Transform中的核心方法，
//    inputs中是传过来的输入流，其中有两种格式，一种是jar包格式一种是目录格式。
//    outputProvider 获取到输出目录，最后将修改的文件复制到输出目录，这一步必须做不然编译会报错
    @Override
    public void transform(Context context,
                          Collection<TransformInput> inputs,
                          Collection<TransformInput> referencedInputs,
                          TransformOutputProvider outputProvider,
                          boolean isIncremental) throws IOException, TransformException, InterruptedException {

    }

}