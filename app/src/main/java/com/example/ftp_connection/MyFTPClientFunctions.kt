package com.example.ftp_connection

import android.util.Log
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.FileOutputStream

class MyFTPClientFunctions {

    private val TAG = "MyFTPClientFunctions"
    lateinit var mFTPClient: FTPClient

    //Method To Connect to FTP server
    fun ftpConnect(host : String, username : String, password : String, port : Int): Boolean {
        try {
            mFTPClient = FTPClient()
            mFTPClient.setConnectTimeout(10 * 1000)
            // connecting to the host
            mFTPClient.connect(host, port)
            //status = mFTPClient.login(username, password)
            // now check the reply code, if positive menu connection sucess
            if (FTPReply.isPositiveCompletion(mFTPClient.replyCode)){
            // login using username & password
                // login using username & password
                val status = mFTPClient.login(username, password)
                /*
				 * Set File Transfer Mode
				 *
				 * To avoid corruption issue you must specified a correct
				 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
				 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
				 * transferring text, image, and compressed files.
				 */
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE)
                mFTPClient.enterLocalPassiveMode()
                return status
            }
        } catch (e : Exception){
            Log.d(TAG, "Error: could not connect to host "+ host)
        }
        return false
    }
    // Method to disconnect from FTP server:
    fun ftpDisconnect() : Boolean{
        try {
            mFTPClient.logout()
            mFTPClient.disconnect()
            return true
        }catch (e : Exception){
            Log.d(TAG, "Error occorred while disconnection from ftp server.")
        }
        return false
    }
    // Method to download a file from FTP server:
    fun ftpDownload(srcFilePath : String, desFilePath : String) : Boolean{
        var status : Boolean = false
        try {
            val desFileStream = FileOutputStream(desFilePath)
            status = mFTPClient.retrieveFile(srcFilePath, desFileStream)
            desFileStream.close()
            return status
        } catch (e : Exception){
            Log.d(TAG, "Download Failed")
        }
        return status
    }

}