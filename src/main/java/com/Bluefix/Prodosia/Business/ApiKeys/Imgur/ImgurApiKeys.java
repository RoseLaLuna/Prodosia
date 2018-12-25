/*
 * Copyright (c) 2018 RoseLaLuna
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.Bluefix.Prodosia.Business.ApiKeys.Imgur;

import com.Bluefix.Prodosia.Business.Imgur.ImgurApi.IImgurManager;
import com.Bluefix.Prodosia.Data.Logger.ILogger;
import com.Bluefix.Prodosia.Data.Storage.CookieStorage;

import java.io.IOException;

/**
 * Business Logic for the Imgur Api Keys
 */
public class ImgurApiKeys
{
    private IImgurManager _imgurManager;
    private CookieStorage _cookieStorage;
    private ILogger _logger;
    private ILogger _userLogger;

    public ImgurApiKeys(
            IImgurManager imgurmanager,
            CookieStorage cookieStorage,
            ILogger logger,
            ILogger userLogger
    )
    {
        _imgurManager = imgurmanager;
        _cookieStorage = cookieStorage;
        _logger = logger;
        _userLogger = userLogger;

        if (_imgurManager == null)
            throw new IllegalArgumentException("The Imgur Manager cannot be null.");
    }


    public void update(String clientId, String clientSecret, String callback)
    {
        // remove the original cookie.
        if (_cookieStorage != null)
        {
            try
            {
                _cookieStorage.setRefreshToken(null);
            } catch (IOException e)
            {
                if (_logger != null)
                    _logger.warn("[ImgurApiKeys] IOException while trying to reset the cookie.\r\n" + e.getMessage());
            }
        }

        // reset the credentials.
        try
        {
            _imgurManager.setCredentials(clientId,
                    clientSecret,
                    callback);
        } catch (Exception e)
        {
            if (_logger != null)
                _logger.error("[ImgurApiKeys] Exception while attempting to reset the Imgur API credentials.\r\n" + e.getMessage());
        }
    }
}