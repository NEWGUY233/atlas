package com.atlas.crmapp.network;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.util.BitmapUtils;
import com.atlas.crmapp.util.FileUtils;
import com.atlas.crmapp.util.ImageUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Harry on 2017-05-08.
 */

public class AliOSS {

    public static interface OnRequestResult {

        public void onSuccess(AliOSSTokenJson aliOSSTokenJson);

        public void onError(DcnException error);
    }

    public static interface OnUpload {

        public void onSuccess(String downloadUrl);

        public void onError(DcnException error);

        public void onProgress(long currentSize, long totalSize);
    }

    public static void getOSSTokenFromServer(Context context, String url, String accessToken, final OnRequestResult onRequestResult) {
        String urlstr = url + "?access_token=" + ((BaseActivity)context).getGlobalParams().getAccessToken();
        HashMap params = new HashMap();

        NetworkUtil.requestJson(context, urlstr, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                AliOSSTokenJson aliOSSTokenJson = gson.fromJson(jsonData, AliOSSTokenJson.class);
                onRequestResult.onSuccess(aliOSSTokenJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    public static void asyncUploadDataToOSS(Context context, String endPoint, String bucketName, final String uploadName, String fileName,
                                            final OSSCredentialProvider credetialProvider, final String downloadUrlPrefix, final OnUpload onUpload) {

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        OSS oss = new OSSClient(context, endPoint, credetialProvider, conf);

        File img = null;
        img = FileUtils.getNewImageFile("upload");
        BitmapUtils.compressBmpToFile(ImageUtil.getThumb(fileName),img);
        if (img != null && img.exists())
            fileName = img.getAbsolutePath();

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucketName, uploadName, fileName);
// 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                onUpload.onProgress(currentSize, totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());

                onUpload.onSuccess(downloadUrlPrefix+uploadName);
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                DcnException err = null;
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    err = new DcnException(-1001, clientExcepion.getLocalizedMessage(), clientExcepion.getMessage());
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    err = new DcnException(Integer.valueOf(serviceException.getErrorCode()), serviceException.getRawMessage(), serviceException.getMessage());
                }

                onUpload.onError(err);
            }
        });
// task.cancel(); // 可以取消任务
// task.waitUntilFinished(); // 可以等待任务完成

    }

    public static void asyncUploadDataToOSS(final Context context, String tokenUrl, String userAccessToken,
                                            final String endPoint, final String bucketName, final String uploadName,
                                            final String fileName, final String downloadUrlPrefix, final OnUpload onUpload) {

        getOSSTokenFromServer(context, tokenUrl, userAccessToken, new OnRequestResult() {
            @Override
            public void onSuccess(final AliOSSTokenJson aliOSSTokenJson) {
                OSSCredentialProvider credetialProvider = new OSSFederationCredentialProvider() {
                    @Override
                    public OSSFederationToken getFederationToken() {
                        try {
//                    URL stsUrl = new URL("http://localhost:8080/distribute-token.json");
//                    HttpURLConnection conn = (HttpURLConnection) stsUrl.openConnection();
//                    InputStream input = conn.getInputStream();
//                    String jsonText = IOUtils.readStreamAsString(input, OSSConstants.DEFAULT_CHARSET_NAME);
//                    JSONObject jsonObjs = new JSONObject(jsonText);
//                    String ak = jsonObjs.getString("accessKeyId");
//                    String sk = jsonObjs.getString("accessKeySecret");
//                    String token = jsonObjs.getString("securityToken");
//                    String expiration = jsonObjs.getString("expiration");
                            return new OSSFederationToken(aliOSSTokenJson.accessKeyId, aliOSSTokenJson.accessKeySecret,
                                    aliOSSTokenJson.securityToken, aliOSSTokenJson.expiration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };

                asyncUploadDataToOSS(context, endPoint, bucketName, uploadName, fileName, credetialProvider, downloadUrlPrefix, new OnUpload() {
                    @Override
                    public void onSuccess(String downloadUrl) {
                        onUpload.onSuccess(downloadUrl);
                    }

                    @Override
                    public void onError(DcnException error) {
                        onUpload.onError(error);
                    }

                    @Override
                    public void onProgress(long currentSize, long totalSize) {
                        onUpload.onProgress(currentSize, totalSize);
                    }
                });
            }

            @Override
            public void onError(DcnException error) {
                Log.e("debug", "onError: "+error.getMessage());
                DcnException err = new DcnException(-1001, error.getLocalizedMessage(), error.getMessage());
                onUpload.onError(err);
            }
        });



    }

}
