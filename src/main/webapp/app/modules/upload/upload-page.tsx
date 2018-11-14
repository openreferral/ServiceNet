import './upload-page.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';

export interface IUploadPageProp extends StateProps, DispatchProps {}

export class UploadPage extends React.Component<IUploadPageProp> {
  render() {
    return (
      <Row>
        <Col>Upload page</Col>
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
