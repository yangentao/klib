package yet.ui.page

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.webkit.*
import android.widget.LinearLayout
import yet.ui.activities.Pages
import yet.ui.ext.*

class WebPage : TitlePage() {

	lateinit var webView: WebView
	private var rootUrl: String? = null
	private var title: String? = null


	@SuppressLint("SetJavaScriptEnabled")
	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		titleBar.showBack()
		titleBar.title(title ?: "")
		webView = WebView(context).genId()
		contentView.addView(webView, linearParam().widthFill().heightDp(0).weight_(1))

		webView.settings.javaScriptEnabled = true
		webView.settings.domStorageEnabled = true


		webView.webViewClient = object : WebViewClient() {
			override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
				view.loadUrl(url)
				return true
			}

			override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
				//                showProgress("加载中...");
			}

			override fun onPageFinished(view: WebView, url: String) {
				//                hideProgress();
			}

		}
		webView.webChromeClient = object : WebChromeClient() {
			override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
				return super.onJsAlert(view, url, message, result)
			}
		}

		CookieSyncManager.createInstance(activity).sync()

		if (rootUrl != null) {
			webView.loadUrl(rootUrl)
		}
	}


	override fun onBackPressed(): Boolean {
		if (webView.canGoBack()) {
			webView.goBack()
			return true
		} else {
			return super.onBackPressed()
		}
	}

	fun loadAsset(assetPath: String) {
		webView.loadUrl("file:///android_asset/" + assetPath)
	}

	companion object {
		fun open(context: Context, title: String, url: String): WebPage {
			val page = WebPage()
			page.rootUrl = url
			page.title = title
			Pages.open(context, page)
			return page
		}
	}

}
