import 'filepond/dist/filepond.min.css';
import './upload-page.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert, Button } from 'reactstrap';
import { FilePond, File, registerPlugin } from 'react-filepond';
import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';

export interface IUploadPageProp extends StateProps, DispatchProps {}

export interface IUploadState {
  files: any[];
  pond: any;
  isUploadDisabled: boolean;
}

registerPlugin(FilePondPluginFileValidateType);
const supportedFileTypes = ['.csv', '.json', 'application/json'];
const maxNumberOfFiles = 100;
const valid = 'valid';

export class UploadPage extends React.Component<IUploadPageProp, IUploadState> {
  state: IUploadState = {
    files: [],
    pond: null,
    isUploadDisabled: true
  };

  onAddFile = (error, file) => {
    if (!error) {
      file.setMetadata(valid, true);
    }
  };

  uploadAll = () => {
    this.state.pond.processFiles();
  };

  isUploadDisabled = () => {
    let result = this.state.files.length === 0;
    this.state.files.forEach(file => {
      if (!file.getMetadata(valid)) {
        result = true;
      }
    });
    return result;
  };

  onUpdateFiles = items => {
    this.setState(
      {
        files: items
      },
      () =>
        this.setState({
          isUploadDisabled: this.isUploadDisabled()
        })
    );
  };

  fileValidateTypeDetectType = (source, type) =>
    new Promise((resolve, reject) => {
      resolve(type);
    });

  render() {
    return (
      <Row>
        <Col>
          <h2>
            <Translate contentKey="upload.title" />
          </h2>
          <p className="lead">
            <Translate contentKey="upload.subtitle" />
          </p>
          <FilePond
            className="dropArea"
            ref={ref => (this.state.pond = ref)}
            allowMultiple
            maxFiles={maxNumberOfFiles}
            server="/api"
            instantUpload={false}
            onaddfile={this.onAddFile}
            acceptedFileTypes={supportedFileTypes}
            fileValidateTypeDetectType={this.fileValidateTypeDetectType}
            onupdatefiles={this.onUpdateFiles}
          >
            {this.state.files.map(fileItem => (
              <File key={fileItem.file} src={fileItem.file} origin="input" />
            ))}
          </FilePond>
          <Button color="primary" disabled={this.state.isUploadDisabled} onClick={this.uploadAll}>
            <Translate contentKey="upload.submit" />
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({});

const mapDispatchToProps = { getSession };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UploadPage);
