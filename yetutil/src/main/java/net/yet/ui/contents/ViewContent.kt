package net.yet.ui.contents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.*

/**
 * Created by entaoyang@163.com on 2016-07-28.
 */

open class ViewContent(val activity: Activity, val arguments: Map<String, Any>) {
	val context: Context get() = activity
	val inflater: LayoutInflater get() = activity.layoutInflater

	init {

	}

	open fun getView(): View? {
		return null
	}

	open fun onCreate(savedInstanceState: Bundle?) {

	}

	open fun onCreateView(parentView: ViewGroup?, savedInstanceState: Bundle?): View? {
		return null
	}

	open fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

	}

	fun onActivityCreated(savedInstanceState: Bundle?) {

	}

	fun onRestart() {

	}

	fun onStart() {

	}


	fun onResume() {

	}

	fun onPause() {

	}

	fun onStop() {

	}

	fun onDestroyView() {

	}

	fun onDestroy() {

	}

	open fun onBackPressed(): Boolean {
		return false
	}

	fun onTouchEvent(event: MotionEvent): Boolean {
		return false
	}

	fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		return false
	}

	fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
		return false
	}

	fun onKeyMultiple(keyCode: Int, repeatCount: Int, event: KeyEvent): Boolean {
		return false
	}

	fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
		return false
	}

	protected fun onNewIntent(intent: Intent) {
	}

	fun onPostCreate(savedInstanceState: Bundle?) {

	}

	fun onPostResume() {

	}

	fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

	}

	fun onConfigurationChanged(newConfig: Configuration) {

	}

	fun onHiddenChanged(hidden: Boolean) {

	}

	open fun onLowMemory() {

	}

	open fun onTrimMemory(level: Int) {

	}

	fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

	}

	fun onSaveInstanceState(outState: Bundle) {
	}

	fun onViewStateRestored(savedInstanceState: Bundle) {
	}

	fun onActivityReenter(resultCode: Int, data: Intent) {
	}

	fun onEnterAnimationComplete() {
	}

	fun onUserInteraction() {
	}

	fun onUserLeaveHint() {
	}

}