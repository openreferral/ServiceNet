import React from 'react';
import { Translate, TextFormat } from 'react-jhipster';
import { connect } from 'react-redux';
import { Table, Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { APP_TIMESTAMP_FORMAT } from 'app/config/constants';

import { getJobs, triggerJob } from './scheduler.reducer';

export interface ISchedulerStateProp extends StateProps, DispatchProps {}

export class SchedulerAdministration extends React.Component<ISchedulerStateProp> {
  componentDidMount() {
    this.props.getJobs();
  }

  triggerJob = job => () => {
    this.props.triggerJob(job);
    this.props.getJobs();
  };

  render() {
    const { jobs } = this.props;
    return (
      <div>
        <h2 id="user-management-page-heading">
          <Translate contentKey="scheduler.title" />
        </h2>
        <Table responsive>
          <thead>
            <tr>
              <th>
                <Translate contentKey="scheduler.name" />
              </th>
              <th>
                <Translate contentKey="scheduler.description" />
              </th>
              <th>
                <Translate contentKey="scheduler.prevFireDate" />
              </th>
              <th>
                <Translate contentKey="scheduler.nextFireDate" />
              </th>
              <th />
            </tr>
          </thead>
          <tbody>
            {jobs.map((job, i) => (
              <tr id={job.name} key={`job-${i}`}>
                <td>{job.name}</td>
                <td>{job.description}</td>
                <td>{job.prevFireDate ? <TextFormat value={job.prevFireDate} type="date" format={APP_TIMESTAMP_FORMAT} /> : null}</td>
                <td>
                  <TextFormat value={job.nextFireDate} type="date" format={APP_TIMESTAMP_FORMAT} />
                </td>
                <td>
                  <Button color="success" onClick={this.triggerJob(job)}>
                    <FontAwesomeIcon icon="play" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="scheduler.trigger" />
                    </span>
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
    );
  }
}

const mapStateToProps = storeState => ({
  jobs: storeState.scheduler.jobs
});

const mapDispatchToProps = { getJobs, triggerJob };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SchedulerAdministration);
