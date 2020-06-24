package connect
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("/screenshot")
    fun getImg(): Call<ResponseBody>
    @POST("/api/command")
    fun sendCom(@Body command: Command) : Call<ResponseBody>
}