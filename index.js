import React, { Component, PureComponent, PropTypes } from 'react';
import {
  View,
  Button,
  UIManager,
  NativeModules,
  requireNativeComponent,
  findNodeHandle,
} from 'react-native';

const ALIBC_TRADEWEBVIEW_REF = 'ALIBCTRADEWEBVIEW_REF';

export class AlibcTradeWebView extends React.Component {
  constructor(props) {
    super(props);
    this._onTradeResult = this._onTradeResult.bind(this);
    this._onStateChange = this._onStateChange.bind(this);
    this.goForward = this.goForward.bind(this);
    this.goBack = this.goBack.bind(this);
    this.reload = this.reload.bind(this);
    this._getWebViewBridgeHandle = this._getWebViewBridgeHandle.bind(this);
  }
  _onTradeResult(event) {
    if (!this.props.onTradeResult) {
      return;
    }
    this.props.onTradeResult(event.nativeEvent);
  }

  _onStateChange(event) {
    if (!this.props.onStateChange) {
      return;
    }
    this.props.onStateChange(event.nativeEvent);
  }

  goForward() {
    UIManager.dispatchViewManagerCommand(
      this._getWebViewBridgeHandle(),
      UIManager.RCTWebViewBridge.Commands.goForward,
      null
    );
  }

  goBack() {
    UIManager.dispatchViewManagerCommand(
      this._getWebViewBridgeHandle(),
      UIManager.AlibcTradeWebView.Commands.goBack,
      null
    );
  }

  reload() {
    UIManager.dispatchViewManagerCommand(
      this._getWebViewBridgeHandle(),
      UIManager.RCTWebViewBridge.Commands.reload,
      null
    );
  }

  _getWebViewBridgeHandle() {
    return findNodeHandle(this.refs[ALIBC_TRADEWEBVIEW_REF]);
  }

  render() {
    return <NativeComponent ref={ALIBC_TRADEWEBVIEW_REF} {...this.props} 
                            onTradeResult={this._onTradeResult} 
                            onStateChange={this._onStateChange}/>;
  }
}

AlibcTradeWebView.propTypes = {
  param: PropTypes.object,
  onTradeResult: React.PropTypes.func,
  onStateChange: React.PropTypes.func,
  ...View.propTypes,
};

const NativeComponent = requireNativeComponent("AlibcTradeWebView", AlibcTradeWebView);

const { RNAlibcSdk } = NativeModules;

export default RNAlibcSdk;
